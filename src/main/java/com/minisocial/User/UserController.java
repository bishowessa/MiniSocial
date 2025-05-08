package com.minisocial.User;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserService userService;

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"User not found\"}")
                    .build();
        }
        return Response.ok(user).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User newData) {
        User updated = userService.updateProfile(id, newData);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"User not found\"}")
                    .build();
        }
        return Response.ok("{\"message\":\"Profile updated\"}").build();
    }

    @GET
    @Path("/search")
    public Response searchUsers(@QueryParam("q") String query) {
        if (query == null || query.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Query parameter 'q' is required\"}")
                    .build();
        }
        return Response.ok(userService.searchUsers(query)).build();
    }

    @GET
    @Path("/{id}/suggested")
    public Response getSuggestedFriends(@PathParam("id") Long userId) {
        return Response.ok(userService.suggestFriends(userId)).build();
    }

    // ❗ Admin-only route filtered by RoleFilter
    @GET
    @Path("/all")
    public Response getAllUsers() {
        return Response.ok(userService.getAllUsers()).build();
    }
}
