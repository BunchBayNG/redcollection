package com.bbng.dao.microservices.auth.passport.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserResponseDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String userType;
    private String phoneNumber;
    private boolean isEnabled;
    private String logoUrl;
    private boolean isInvitedUser;
}
