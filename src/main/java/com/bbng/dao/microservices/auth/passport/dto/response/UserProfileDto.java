package com.bbng.dao.microservices.auth.passport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String email;
    private String profilePhoto;
}
