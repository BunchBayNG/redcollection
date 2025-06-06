package com.bbng.dao.microservices.auth.passport.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String username;
    private String phoneNumber;
}
