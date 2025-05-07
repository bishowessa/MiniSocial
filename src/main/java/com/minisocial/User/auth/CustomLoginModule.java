package com.minisocial.User.auth;

import javax.security.auth.spi.LoginModule;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import java.util.*;

public class CustomLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private String email;
    private String role;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
                           Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback nameCb = new NameCallback("Email: ");
        PasswordCallback passCb = new PasswordCallback("Password: ", false);
        try {
            callbackHandler.handle(new Callback[]{nameCb, passCb});
            this.email = nameCb.getName();
            String password = new String(passCb.getPassword());

            // Simulated DB check
            if ("admin@example.com".equals(email) && "admin123".equals(password)) {
                role = "admin";
                return true;
            } else if ("user@example.com".equals(email) && "user123".equals(password)) {
                role = "user";
                return true;
            } else {
                throw new FailedLoginException("Invalid credentials");
            }
        } catch (Exception e) {
            throw new LoginException(e.getMessage());
        }
    }

    @Override
    public boolean commit() throws LoginException {
        subject.getPrincipals().add(new UserPrincipal(email));
        subject.getPrincipals().add(new RolePrincipal(role));
        return true;
    }

    @Override public boolean abort() { return true; }
    @Override public boolean logout() { return true; }
}
