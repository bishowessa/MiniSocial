package com.minisocial.User.auth;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.minisocial.User.User;
import com.minisocial.User.UserService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    public Response register(User user) {
        if (userService.findByEmail(user.getEmail()) != null) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("{\"error\":\"Email already exists\"}")
                           .build();
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user"); // default role
        }
        userService.register(user);
        return Response.status(Response.Status.CREATED)
                       .entity("{\"message\":\"User registered successfully\"}")
                       .build();
    }

    @POST
    @Path("/login")
    public Response login(User loginRequest) {
        User found = userService.findByEmail(loginRequest.getEmail());
        if (found == null || !found.getPassword().equals(loginRequest.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("{\"error\":\"Invalid email or password\"}")
                           .build();
        }

        // 👇 Simulate a token (you can replace with real JWT later)
        String token = "token_" + found.getId() + "_" + System.currentTimeMillis();

        return Response.ok("{\"message\":\"Login successful\", \"userId\":" + found.getId() +
                           ", \"role\":\"" + found.getRole() + "\", \"token\":\"" + token + "\"}")
                       .build();
    }
}
