package service;

import entity.Board;
import entity.ListEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Stateless
@Path("/lists")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class listService {

    @PersistenceContext(unitName = "hello")
    private EntityManager entityManager;
    // Create a new list for a board (TeamLeader Role only)
    @POST
    @Path("/createList/{boardId}/{listName}")
    public Response createList(
            @PathParam("boardId") String boardId, 
            @PathParam("listName") String listName) {
        try {
            // Check if the current user is a team leader
            if (!userService.currentUser.getTeamLeaderStatus()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Only team leaders can create lists").build();
            }

            // Find the board by ID
            Board board = entityManager.find(Board.class, boardId);

            // Check if the board exists
            if (board == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Board not found").build();
            }
            
            // Create a new list
            ListEntity newList = new ListEntity(listName);
            newList.setBoard(board);

            // Persist the new list
            entityManager.persist(newList);

            // Add the new list to the board
            board.getListsInBoard().add(newList);

            // Update the board to reflect the changes
            entityManager.merge(board);
            
            
            return Response.ok("List created successfully").build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }


    // Delete a list by name (TeamLeader Role only)
    @DELETE
    @Path("/deleteList/{boardId}/{listName}")
    public Response deleteList(@PathParam("boardId") String boardId, @PathParam("listName") String listName) {
        try {
            // Check if the current user is a team leader
            if (!userService.currentUser.getTeamLeaderStatus()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Only team leaders can delete lists").build();
            }

            // Find the board by ID
            Board board = entityManager.find(Board.class, boardId);

            // Check if the board exists
            if (board == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Board not found").build();
            }

            // Find the list by name
            List<ListEntity> listsInBoard = board.getListsInBoard();
            ListEntity listToDelete = null;
            for (ListEntity listEntity : listsInBoard) {
                if (listEntity.getName().equals(listName)) {
                    listToDelete = listEntity;
                    break;
                }
            }

            // Check if the list exists
            if (listToDelete == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("List not found in the specified board").build();
            }
            
            listsInBoard.remove(listToDelete);
            // Delete the list
            entityManager.remove(listToDelete);
            

            return Response.ok("List deleted successfully").build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }

}