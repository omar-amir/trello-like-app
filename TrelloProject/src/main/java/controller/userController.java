package controller;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import entity.user;
import service.userService;
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class userController {
    @EJB
    private userService userService;
    @POST
    @Path("/register")
    public Response registerUser(user newUser) {
        return userService.registerUser(newUser);
    }
    @POST
    @Path("/log_in/{email}/{password}")
    public Response logIn(@PathParam("email") String email, @PathParam("password") String password) {
        return userService.logIn(email, password);
    }

    @PUT
    @Path("/{userId}")
    public Response updateUserProfile(@PathParam("userId") String userId, user updatedUser) {
        return userService.updateUserProfile(userId, updatedUser);
    }
}
