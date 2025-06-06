package com.bbng.dao.microservices.auth.passport.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ResetPasswordResponse {
    private String code;
    private String message;
    private Authentication authentication;
}
