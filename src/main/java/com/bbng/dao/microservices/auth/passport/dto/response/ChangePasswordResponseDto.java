package com.bbng.dao.microservices.auth.passport.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChangePasswordResponseDto {
    private String message;
    private String email;
    private HttpStatus httpStatus;
}
