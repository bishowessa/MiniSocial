package com.minisocial.User.auth;

import java.security.Principal;

public class RolePrincipal implements Principal {
    private String role;

    public RolePrincipal(String role) { this.role = role; }

    @Override
    public String getName() { return role; }
}
