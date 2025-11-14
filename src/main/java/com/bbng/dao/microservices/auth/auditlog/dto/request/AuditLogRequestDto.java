package com.bbng.dao.microservices.auth.auditlog.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuditLogRequestDto {

    private String userId;
    private String userName;
    private String email;
    private String userRole;
    private String userIpAddress;
    private String merchantName;
    private String merchantId;
    private String merchantOrganization;
    private String merchantOrgId;
    private String event;
    private String userType;
    private String description;
    private Boolean isDeleted;


}
