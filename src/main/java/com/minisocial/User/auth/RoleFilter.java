package com.minisocial.User.auth;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider; 
import java.io.IOException;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class RoleFilter implements ContainerRequestFilter {

	
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    	System.out.println("🔥 Filter triggered on path: " + requestContext.getUriInfo().getPath());
    	String token = null;

        // 1. Try cookie
        Cookie cookie = requestContext.getCookies().get("AuthToken");
        if (cookie != null) {
            token = cookie.getValue();
        }

        // 2. Fallback to Authorization header
        if (token == null) {
            String header = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring("Bearer ".length());
            }
        }

        // 3. Reject if no token
        if (token == null) {
            deny(requestContext, "Missing token");
            return;
        }

        // 4. Decode role
        String role = JwtUtil.extractRole(token);
        if (role == null) {
            deny(requestContext, "Invalid token");
            return;
        }

        String path = requestContext.getUriInfo().getPath();

        // 5. Enforce admin-only route
        if (path.contains("/user/all") && !"admin".equals(role)) {
            deny(requestContext, "Admin access only");
        }
    }

    private void deny(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"" + message + "\"}")
                .build());
    }
}
