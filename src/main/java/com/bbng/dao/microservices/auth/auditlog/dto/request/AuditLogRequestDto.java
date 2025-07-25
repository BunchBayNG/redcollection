package com.bbng.dao.microservices.auth.auditlog.dto.request;

import com.bbng.dao.microservices.auth.auditlog.Events;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuditLogRequestDto {

    private String event;
    private String userId;
    private String userName;
    private String email;
    private String userRole;
    private String userIpAddress;
    private String userType;
    private String merchantName;
    private String merchantId;
    private String description;
    private Boolean isDeleted;
}
