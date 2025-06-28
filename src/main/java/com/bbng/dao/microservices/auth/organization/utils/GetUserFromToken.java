package com.bbng.dao.microservices.auth.organization.utils;


import com.bbng.dao.microservices.auth.organization.entity.ApiKeyEntity;
import com.bbng.dao.microservices.auth.organization.repository.APIKeyRepository;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.util.exceptions.customExceptions.TokenNotValidException;
import com.bbng.dao.util.exceptions.customExceptions.UnauthorizedException;
import com.bbng.dao.util.exceptions.customExceptions.UserNotFoundException;
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


    public static String extractUserFromApiKey(HttpServletRequest request, APIKeyRepository apiKeyRepository, UserRepository userRepository) {
        // get the header
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")){
            throw new UnauthorizedException("You are not authorized to make this request, please log in!");
        }
        //get the token
        String token = header.substring(7);
        if(token.startsWith("TEST")){
            ApiKeyEntity testKey = apiKeyRepository.findByTestKey(token).orElseThrow(() -> new TokenNotValidException("Invalid apiKey token, please check your token"));
            UserEntity user = userRepository.findById(testKey.getUserId()).orElseThrow(() -> new UserNotFoundException("Can't find user that has this apiKey"));
            return user.getEmail();
        }
        ApiKeyEntity liveKey = apiKeyRepository.findByLiveKey(token).orElseThrow(() -> new TokenNotValidException("Invalid apiKey token, please check your token"));
        UserEntity user = userRepository.findById(liveKey.getUserId()).orElseThrow(() -> new UserNotFoundException("Can't find user that has this apiKey"));
        return user.getEmail();
    }

}
