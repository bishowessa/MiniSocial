package com.minisocial.User.auth;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
            user.setRole("user");
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
                           .entity("{\"error\":\"Invalid credentials\"}")
                           .build();
        }

        String token = JwtUtil.generateToken(found.getEmail(), found.getRole());

        NewCookie cookie = new NewCookie("AuthToken", token, "/", "", "Auth token", 3600, false);
        return Response.ok("{\"message\":\"Login successful\", \"token\": \"" + token + "\"}")
                       .cookie(cookie)
                       .build();
    }
}
