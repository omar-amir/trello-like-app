package service;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import entity.user;
import java.util.List;
@Stateless
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class userService {
	public static user currentUser;
    @PersistenceContext(unitName = "hello")
    private EntityManager entityManager;
    @POST
    @Path("/register")
    public Response registerUser(user newUser) {
        try {
            entityManager.persist(newUser);
            return Response.ok(newUser).build();
        } 
        catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }
    @POST
    @Path("/log_in/{email}/{password}")
    public Response logIn(@PathParam("email") String email, @PathParam("password") String password) {
        try {
            // Find the user with the given email
            TypedQuery<user> query = entityManager.createQuery("SELECT u FROM user u WHERE u.email = :email", user.class);
            query.setParameter("email", email);
            List<user> users = query.getResultList();

            // Check if a user with the given email exists
            if (!users.isEmpty()) {
                user loggedInUser = users.get(0); // Assuming unique email constraint

                // Check if the provided password matches the user's password
                if (loggedInUser.getPassword().equals(password)) {
                    // Set the current user
                    currentUser = loggedInUser;

                    // Return the logged-in user
                    return Response.ok(loggedInUser).build();
                } else {
                    return Response.status(Response.Status.UNAUTHORIZED).build(); // Incorrect password
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).build(); // User not found
            }
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage())
                    .build();
        }
    }
    @PUT
    @Path("/{userId}")
    public Response updateUserProfile(@PathParam("userId") String userId, user updatedUser) {
        try {
            user existingUser = entityManager.find(user.class, userId);
            if (existingUser != null) {
                // Update the user's profile information
                existingUser.setName(updatedUser.getName());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setPassword(updatedUser.getPassword());
                existingUser.setTeamLeaderStatus(updatedUser.getTeamLeaderStatus());
                entityManager.merge(existingUser);
                return Response.ok(existingUser).build();
            } 
            else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } 
        catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }
    @GET
    @Path("/getCurrentUser")
    public Response getCurrentUser() {
        if (currentUser != null) {
            return Response.ok(currentUser).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("There is No logged in user").build();
        }
    }
    @GET
    @Path("/getallusers")
    public Response getAllUsers() {
        try {
            TypedQuery<user> query = entityManager.createQuery("SELECT u FROM user u", user.class);
            List<user> users = query.getResultList();
            return Response.ok(users).build();
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }
}