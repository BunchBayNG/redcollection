package com.bbng.dao.microservices.auth.auditlog.dto.request;

import com.bbng.dao.microservices.auth.auditlog.Events;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuditLogFilterRequest {
    private String search;         // userName, email, merchantName
    private String userRole;
    private Events event;
    private LocalDate startDate;
    private LocalDate endDate;

    private String sortBy;         // userName or time
    private String sortOrder;      // ASC or DESC

    private int page = 0;
    private int size = 10;

    // Getters and Setters
}
