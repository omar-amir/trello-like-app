package service;
import entity.Board;
import entity.Card;
import entity.ListEntity;
import entity.user;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
@Stateless
@Path("/boards")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class boardService {

    @PersistenceContext(unitName = "hello")
    private EntityManager entityManager;
    private boolean isBoardNameUnique(String boardName) {
        // Query the database to check if any board already exists with the given name
        TypedQuery<Board> query = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :boardName", Board.class);
        query.setParameter("boardName", boardName);
        List<Board> existingBoards = query.getResultList();

        // If no boards are found with the given name, return true
        return existingBoards.isEmpty();
    }

    @POST
    @Path("/createBoard")
    public Response createBoard(Board newBoard) {
        try {
        	 if (userService.currentUser == null) {
                 return Response.status(Response.Status.UNAUTHORIZED)
                         .entity("There is no logged in user").build();
             }
            // Check if the current user is a team leader
            if (!userService.currentUser.getTeamLeaderStatus()) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Only team leaders can create boards").build();
            }

            // Check if the board name is unique
            if (!isBoardNameUnique(newBoard.getName())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Board name must be unique").build();
            }

            // Save the new board
            newBoard.setOwnerTeamLeader(userService.currentUser);
//            newBoard.addCollaborator(new user());
//            newBoard.getListsInBoard().add(new ListEntity());
            entityManager.persist(newBoard);
            return Response.ok(newBoard).build();
        } 
        catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }


    @GET
    @Path("/getUserBoards")
    public Response getUserBoards() {
        try {
            if (userService.currentUser == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("There is no logged in user").build();
            }

            // Check if the current user is a team leader
            if (!userService.currentUser.getTeamLeaderStatus()) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Only team leaders can access this endpoint").build();
            }

            TypedQuery<Board> query = entityManager.createQuery(
                    "SELECT b FROM Board b WHERE b.ownerTeamLeader.userId = :userId",
                    Board.class
            );

            query.setParameter("userId", userService.currentUser.getUserId());
            List<Board> boards = query.getResultList();
            
            // Create a list to hold the DTOs
            List<BoardDTO> boardDTOs = new ArrayList<>();

            // Iterate through each board
            for (Board board : boards) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setBoardId(board.getBoardId());
                boardDTO.setName(board.getName());
                boardDTO.setOwner(convertUserToUserDTO(board.getOwnerTeamLeader()));
                boardDTO.setCollaborators(convertUsersToUserDTOs(board.getCollaborators()));
                boardDTO.setListsInBoard(convertListsInBoardToListEntityDTOs(board.getListsInBoard()));
                boardDTOs.add(boardDTO);
            }

            return Response.ok(boardDTOs).build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }

    // Conversion methods
    private UserDTO convertUserToUserDTO(user user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setName(user.getName());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setTeamLeaderStatus(user.getTeamLeaderStatus());
        return userDTO;
    }

    private List<UserDTO> convertUsersToUserDTOs(List<user> users) {
        List<UserDTO> userDTOs = new ArrayList<>();
        for (user _user : users) {
            userDTOs.add(convertUserToUserDTO(_user));
        }
        return userDTOs;
    }

    private List<ListEntityDTO> convertListsInBoardToListEntityDTOs(List<ListEntity> listsInBoard) {
        List<ListEntityDTO> listEntityDTOs = new ArrayList<>();
        for (ListEntity listEntity : listsInBoard) {
            ListEntityDTO listEntityDTO = new ListEntityDTO();
            listEntityDTO.setListId(listEntity.getListId());
            listEntityDTO.setName(listEntity.getName());
            // Assuming ListEntityDTO has appropriate attributes and conversion methods for cards
            listEntityDTO.setCards(convertCardsToCardDTOs(listEntity.getCards()));
            listEntityDTOs.add(listEntityDTO);
        }
        return listEntityDTOs;
    }

    private List<CardDTO> convertCardsToCardDTOs(List<Card> cards) {
        List<CardDTO> cardDTOs = new ArrayList<>();
        for (Card card : cards) {
            CardDTO cardDTO = new CardDTO();
            cardDTO.setCardId(card.getCardId());
            cardDTO.setDescription(card.getDescription());
            cardDTO.setAssigneeId(card.getAssigneeId());
            cardDTO.setStatus(card.getStatus());
            cardDTO.setComment(card.getComment());
            // Assuming CardDTO has appropriate attributes
            cardDTOs.add(cardDTO);
        }
        return cardDTOs;
    }



    
    
    
    
   


 // Invite other users to collaborate on a board (TeamLeader Role only)
    @PUT
    @Path("/{boardId}/{userId}")
    public Response inviteCollaborator(@PathParam("boardId") String boardId, @PathParam("userId") String userId) {
        try {
            // Check if the current user is a team leader
            if (!userService.currentUser.getTeamLeaderStatus()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Only team leaders can invite collaborators").build();
            }

            // Retrieve the board
            Board board = entityManager.find(Board.class, boardId);
            if (board == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Board not found").build();
            }

            // Add the user to the collaborators list
            user collaborator = entityManager.find(user.class, userId);
            if (collaborator == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found").build();
            }

            // Check if the user is already a collaborator
            if (board.getCollaborators().contains(collaborator)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("User is already a collaborator").build();
            }

            // Add the user to the collaborators list
            board.getCollaborators().add(collaborator);
            entityManager.persist(board);

            return Response.ok("User invited successfully").build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }


 // Delete a board (TeamLeader Role only)
    @DELETE
    @Path("/{boardId}")
    public Response deleteBoard(@PathParam("boardId") String boardId) {
        try {
            if (!userService.currentUser.getTeamLeaderStatus()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Only team leaders can delete boards").build();
            }

            // Find the board by ID
            Board boardToDelete = entityManager.find(Board.class, boardId);
            
            // Check if the board exists
            if (boardToDelete == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Board not found").build();
            }

            // Delete the board
            entityManager.remove(boardToDelete);

            return Response.ok("Board deleted successfully").build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }
    @GET
    @Path("/collaborators/{boardId}")
    public Response getBoardCollaborators(@PathParam("boardId") String boardId) {
        try {
            // Find the board by ID
            Board board = entityManager.find(Board.class, boardId);

            // Check if the board exists
            if (board == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Board not found").build();
            }

            // Get the list of collaborators
            List<UserDTO> collaborators = convertUsersToUserDTOs(board.getCollaborators());

            return Response.ok(collaborators).build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }

}
