package com.bbng.dao.microservices.auth.passport.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthenticationFilter authenticationFilter;
    private JwtAuthEntryPoint authEntryPoint;
    private LogoutService logoutService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(
                Arrays.asList(
                        "https://app.redtechlimited.com",
                        "http://localhost:5000"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type",
                "Accept", "Authorization", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept",
                "Access-Control-Allow-Origin", "Authorization"));
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "PUT", "PATCH", "DELETE", "OPTIONS", "GET", "HEAD"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(c ->
                        c.configurationSource(corsConfigurationSource())
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(httpRequests ->
                        httpRequests.
                                requestMatchers(
                                        "/api/v1/redtech/general-mgt/**",
                                        "/api/v1/redtech/auth/**",
                                        "/api/v1/redtech/email/sendMail",
                                        "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "/docs"
                                ).permitAll()
                                .anyRequest().authenticated());
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.logout(logout -> logout
                .logoutUrl("api/v1/regportal/logout")
                .addLogoutHandler(logoutService)
                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


//    "api/v1/regportal/cohorts/applicants/{cohortId}",
//                                        "api/v1/regportal/cohorts/applicants/{organizationId}",
//                                        "api/v1/regportal/cohorts/applicants/by-organization&cohort",
//                                        "api/v1/regportal/assessment/update",
//                                        "api/v1/regportal/assessment/add-question-to-assessment/{assessmentId}",
//                                        "api/v1/regportal/assessment/{assessmentId}",
//                                        "/api/v1/regportal/assessment/batch/createQuestion/**",
//                                        "/api/v1/regportal/assessment/update-question",
//                                        "/api/v1/regportal/assessment/delete-question/{questionId}",
}
