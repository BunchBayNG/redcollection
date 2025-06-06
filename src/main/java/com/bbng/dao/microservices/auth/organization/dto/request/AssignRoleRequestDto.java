package com.bbng.dao.microservices.auth.organization.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignRoleRequestDto {
    private String adminId;
    private String email;
    private String roleName;
    private String roleId;
    private List<Long> permissionsIds;
}
