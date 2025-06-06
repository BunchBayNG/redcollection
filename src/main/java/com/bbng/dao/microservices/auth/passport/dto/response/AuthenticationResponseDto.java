package com.bbng.dao.microservices.auth.passport.dto.response;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuthenticationResponseDto {
    private String message;
    private Authentication authentication;
    private String userId;
    private String username;

    private String organizationId;
    private String organizationName;
    private String userType;

    private List<String> roles;

    private String token;
    private String refreshToken;
}
