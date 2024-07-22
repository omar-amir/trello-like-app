package service;

import entity.Board;
import entity.Card;
import entity.ListEntity;
import entity.user;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/cards")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardService {

    @PersistenceContext
    private EntityManager entityManager;
    
    @POST
    @Path("/createCard/{boardName}/{listId}/{userid}")
    public Response createCard(
            @PathParam("boardName") String boardName,
            @PathParam("listId") String listId,
            @PathParam("userid") String userid) {
        try {
            TypedQuery<Board> boardQuery = entityManager.createQuery(
                "SELECT b FROM Board b WHERE b.name = :boardName",
                Board.class
                
            );
            boardQuery.setParameter("boardName", boardName);
            List<Board> boards = boardQuery.getResultList();
            
            
            user userr = entityManager.createQuery("SELECT u FROM user u WHERE u.userId = :userid", user.class)
                    .setParameter("userid", userid)
                    .getSingleResult();
            
            if (userService.currentUser == null || boards.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User or board not found").build();
            }
            
            Board board = boards.get(0);
            ListEntity list = entityManager.find(ListEntity.class, listId);

            // Check if the list exists and belongs to the specified board
            if (list == null || !board.getListsInBoard().contains(list)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("The specified list does not belong to the board").build();
            }

            // Check if the current user is a team leader or collaborator of the board
            if (!userService.currentUser.getTeamLeaderStatus() && !board.getCollaborators().contains(userr)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Only team leaders and collaborators of the board can create cards").build();
            }

            // Create a new empty card
            Card card = new Card();
            card.setListEntity(list);
            entityManager.persist(card);
            
            // Add the card to the list
            list.getCards().add(card);

            return Response.ok("Card created successfully").build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }


    @PUT
    @Path("/moveCard/{cardId}/{newListId}/{userid}")
    public Response moveCard(
            @PathParam("cardId") String cardId,
            @PathParam("newListId") String newListId,
            @PathParam("userid") String userid) {
        try {
            // Find the card
            Card card = entityManager.find(Card.class, cardId);
            if (card == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Card not found").build();
            }
            
            // Find the new list
            ListEntity newList = entityManager.find(ListEntity.class, newListId);
            if (newList == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("List not found").build();
            }
            user userr = entityManager.createQuery("SELECT u FROM user u WHERE u.userId = :userid", user.class)
                    .setParameter("userid", userid)
                    .getSingleResult();
            // Check if the current user is authorized to move the card
            if (!userService.currentUser.getTeamLeaderStatus() && !card.getListEntity().getBoard().getCollaborators().contains(userr)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Only team leaders and collaborators of the board can move cards").build();
            }

            // Remove the card from its current list
            ListEntity currentList = card.getListEntity();
            currentList.getCards().remove(card);

            // Add the card to the new list
            newList.getCards().add(card);
            card.setListEntity(newList);

            // Persist the changes
            entityManager.merge(currentList);
            entityManager.merge(newList);
            entityManager.merge(card);

            return Response.ok("Card moved successfully").build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }
    @PUT
    @Path("/assignCard/{cardId}/{userid}/{assigneeId}")
    public Response assignCard(
            @PathParam("cardId") String cardId,
            @PathParam("userid") String userid,
            @PathParam("assigneeId") String assigneeId) {
        try {
            // Find the card
            Card card = entityManager.find(Card.class, cardId);
            if (card == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Card not found").build();
            }
           //Find the assignor user
            user userr = entityManager.createQuery("SELECT u FROM user u WHERE u.userId = :userid", user.class)
                    .setParameter("userid", userid)
                    .getSingleResult();
            
            // Check if the current user is authorized to assign the card
            if (!userr.getTeamLeaderStatus() && !card.getListEntity().getBoard().getCollaborators().contains(userr)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Only team leaders and collaborators of the board can assign cards").build();
            }
            
            // Find the user to assign the card to
            user assignee = entityManager.find(user.class, assigneeId);
            if (assignee == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Assignee not found").build();
            }
            

          

            // Check if the assignee is a collaborator in the board
            Board board = card.getListEntity().getBoard();
            if (!assignee.getTeamLeaderStatus() && !board.getCollaborators().contains(assignee)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Assignee must be a collaborator in the board").build();
            }

            // Update the assignee of the card
            card.setAssigneeId(assigneeId);

            // Persist the changes
            entityManager.merge(card);

            return Response.ok("Card assigned successfully").build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }
    @PUT
    @Path("/addCardDescription/{cardId}/{userId}/{description}")
    public Response addCardDescription(
            @PathParam("cardId") String cardId,
            @PathParam("userId") String userId,
            @PathParam("description") String description) {
        try {
            // Find the card
            Card card = entityManager.find(Card.class, cardId);
            if (card == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Card not found").build();
            }

            // Find the user
            user userr = entityManager.find(user.class, userId);
            if (userr == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found").build();
            }

            // Check if the current user is authorized to update the card
            if (!userr.getTeamLeaderStatus() && !card.getListEntity().getBoard().getCollaborators().contains(userr)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Only team leaders and collaborators of the board can update cards").build();
            }

            // Set the card description
            card.setDescription(description);

            // Persist the changes
            entityManager.merge(card);

            return Response.ok("Card description added successfully").build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }

    
    @PUT
    @Path("/updateCardStatus/{cardId}/{userid}/{status}")
    public Response updateCardStatus(
            @PathParam("cardId") String cardId,
            @PathParam("userid") String userid,
            @PathParam("status") String status) {
        try {
            // Find the card
            Card card = entityManager.find(Card.class, cardId);
            if (card == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Card not found").build();
            }
            //Find the assignor user
            user userr = entityManager.createQuery("SELECT u FROM user u WHERE u.userId = :userid", user.class)
                    .setParameter("userid", userid)
                    .getSingleResult();
            
            // Check if the current user is authorized to update the card
            if (!userr.getTeamLeaderStatus() && !card.getListEntity().getBoard().getCollaborators().contains(userr)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Only team leaders and collaborators of the board can update cards").build();
            }

         // Update the card status
            card.setStatus(status);

            // Persist the changes
            entityManager.merge(card);

            return Response.ok("Card status updated successfully").build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }
    
    
    @PUT
    @Path("/addCardComment/{cardId}/{userid}/{comment}")
    public Response addCardComment(
            @PathParam("cardId") String cardId,
            @PathParam("userid") String userid,
            @PathParam("comment") String comment) {
        try {
            // Find the card
            Card card = entityManager.find(Card.class, cardId);
            if (card == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Card not found").build();
            }
            //Find the assignor user
            user userr = entityManager.createQuery("SELECT u FROM user u WHERE u.userId = :userid", user.class)
                    .setParameter("userid", userid)
                    .getSingleResult();
            
            // Check if the current user is authorized to update the card
            if (!userr.getTeamLeaderStatus() && !card.getListEntity().getBoard().getCollaborators().contains(userr)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Only team leaders and collaborators of the board can update cards").build();
            }

            // Add the card comment
            card.setComment(comment);

            // Persist the changes
            entityManager.merge(card);

            return Response.ok("Card comment added successfully").build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }


}
