package com.example.newboard.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class SecurityUtil {
    public static String extractEmail(Authentication auth) {
        System.out.println("👉 extractEmail() called");
        if (auth == null) {
            System.out.println("👉 Authentication is NULL");
            return null;
        }

        Object principal = auth.getPrincipal();
        System.out.println("👉 Principal class = " + principal.getClass().getName());
        System.out.println("👉 Principal toString = " + principal.toString());

        if (principal instanceof OidcUser oidcUser) {
            System.out.println("👉 OIDC attributes = " + oidcUser.getAttributes());
            return oidcUser.getEmail();
        } else if (principal instanceof org.springframework.security.core.userdetails.User user) {
            return user.getUsername();
        }

        throw new IllegalStateException("지원하지 않는 Principal 타입: " + principal.getClass());
    }
}


