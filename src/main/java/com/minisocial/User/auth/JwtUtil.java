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
            String[] parts = decoded.split(":");
            return parts[1]; // email:role
        } catch (Exception e) {
            return null;
        }
    }

    public static String extractEmail(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");
            return parts[0]; // email:role
        } catch (Exception e) {
            return null;
        }
    }
}
