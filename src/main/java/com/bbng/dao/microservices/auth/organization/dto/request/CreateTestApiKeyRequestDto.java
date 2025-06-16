package com.bbng.dao.microservices.auth.organization.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTestApiKeyRequestDto{
    private String email;
    private String password;
}
