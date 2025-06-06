package com.bbng.dao.microservices.auth.auditlog.dto.request;

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
    private String merchantId;
    private String merchantName;
    private String userType;
    private Instant dateTimeStamp;
    private boolean isDeleted;
    private boolean succeeded;
    private String description;
}
