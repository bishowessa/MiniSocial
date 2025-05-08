package com.minisocial.User.auth;

import java.util.Base64;

public class JwtUtil {

    public static String generateToken(String email, String role) {
        String payload = email + ":" + role;
        return Base64.getEncoder().encodeToString(payload.getBytes());
    }

    public static String extractRole(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            return decoded.split(":")[1];
        } catch (Exception e) {
            return null;
        }
    }

    public static String extractEmail(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            return decoded.split(":")[0];
        } catch (Exception e) {
            return null;
        }
    }
}
