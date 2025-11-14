package com.bbng.dao.microservices.auth.auditlog.dto.response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuditLogResponseDto {

    private String id;
    private String userName;
    private String email;
    private String userRole;
    private String userIpAddress;
    private String merchantName;
    private String event;
    private String description;

}
