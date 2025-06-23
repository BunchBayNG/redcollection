package com.bbng.dao.microservices.auth.organization.utils;


import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.util.exceptions.customExceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor

public class GetUserFromToken {
    public static String extractTokenFromHeader(HttpServletRequest request, JWTService jwtService){
        // get the header
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")){
            throw new UnauthorizedException("You are not authorized to make this request, please log in!");
        }
        //get the token
        String token = header.substring(7);
        //get the username from the token
        return jwtService.getUsername(token);

    }

}
