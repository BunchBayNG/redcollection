package com.bbng.dao.microservices.auth.passport.config;


import com.bbng.dao.microservices.auth.passport.repository.APIKeyRepository;
import com.bbng.dao.microservices.auth.passport.repository.TokenRepository;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    /**
     * the request, the response, consider filter chain like a pointer to the next filter in the chain
     * extract the authorization details from the request.
     * check whether the details is null or does not start with bearer.
     * if either of the above conditions, go to the next filter in the chain.
     * if both are fine,
     * get the token from the authorization header.
     * from the token, get the username. if username is not null && the user has not already been authenticated,
     * perform the following steps:
     * obtain the user details from the custom userServiceDetails class.
     * check if the token is valid (remember token expiration and that the user matches it)
     * if valid.
     * create an authentication object.
     * reinforce the details of the authentication object
     * set the security context holder with this authentication object
     * pass it to the next step in the filter chain.
     */

    private JWTService jwtService;
    private PassportUserDetailsService userDetailsService;
    private TokenRepository tokenRepository;
    private APIKeyRepository apiKeyRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token from header
        String token = authHeader.substring(7).strip();

        // Handle API keys separately
        if (token.startsWith("TEST") || token.startsWith("LIVE")) {
            log.info("Detected API KEY");

            boolean isTestKey = apiKeyRepository.existsByTestKey(token);
            boolean isLiveKey = apiKeyRepository.existsByLiveKey(token);

            if (!(isLiveKey || isTestKey)) {
                log.warn("Invalid API Key: {}", token);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
                return;
            }
            log.info("filtering request");
            filterChain.doFilter(request, response);
            log.info("done with apiKey");
        } else {
            // Proceed with JWT processing for non-API-key tokens
            log.info("still procceed with jwt");
            String username;
            try {
                log.info("Processing JWT");
                username = jwtService.getUsername(token);
                log.info("Username extracted from JWT: {}", username);
            } catch (Exception e) {
                log.error("Error occurred while parsing JWT: ", e);
                filterChain.doFilter(request, response);
                log.info("done");
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                Boolean isValid = tokenRepository.findByToken(token)
                        .map(t -> !t.getRevoked() && !t.getExpired())
                        .orElse(false);

                if (jwtService.isTokenValid(token, userDetails) && isValid) {
                    // Extract roles from the existing token
                    List<String> roles = jwtService.getRolesFromToken(token);

                    // Convert roles to SimpleGrantedAuthority
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    // Create an authentication object and set it in the SecurityContextHolder
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, token, authorities);

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        }
    }


}