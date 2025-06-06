package com.bbng.dao.microservices.auth.passport.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class ApiKeyAuthEntryPoint  implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Log the error for debugging
        System.out.println("Unauthorized access: " + authException.getMessage());

        // Respond with a 401 Unauthorized status
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid API Key or missing API Key");
    }
}
