package com.bbng.dao.microservices.auth.organization.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InviteRequestDto {

    private String organizationId;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Optional<String> message;
}
