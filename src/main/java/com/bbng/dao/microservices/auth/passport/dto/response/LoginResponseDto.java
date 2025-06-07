package com.bbng.dao.microservices.auth.passport.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String acctStatus;
    private String userId;
    private String organizationId;
    private boolean isEmailVerified;
}
