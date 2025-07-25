package com.example.jobhunt.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            System.out.println("✅ Google login successful");
            response.sendRedirect("/home");
        } else if (principal instanceof UserDetails) {
            System.out.println("✅ Form login successful");
            response.sendRedirect("/home");
        } else {
            System.out.println("❌ Unknown principal type: " + principal);
            response.sendRedirect("/login?error=unknown");
        }
    }
}
