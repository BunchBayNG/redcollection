package com.bbng.dao.microservices.auth.passport.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MfaDto {

    private boolean isLogin;
    private String token;
}
