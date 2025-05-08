package com.minisocial.User;

import com.minisocial.User.auth.RoleFilter;
import com.minisocial.User.auth.AuthController;

import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(UserController.class);
        resources.add(AuthController.class);
        resources.add(RoleFilter.class); // ✅ REGISTER IT!
        return resources;
    }
}
