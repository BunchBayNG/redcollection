package com.bbng.dao.microservices.auth.auditlog.controllers;

import com.bbng.dao.microservices.auth.auditlog.entities.AuditLogEntity;
import com.bbng.dao.microservices.auth.auditlog.repository.AuditLogRepository;
import com.bbng.dao.microservices.auth.auditlog.service.AuditLogService;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.PermissionService;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("${apiVersion}" + "/audit-log")
public class AuditLogController {


    private final String defaultPage = "0";      // default page
    private final String defaultSize = "10";     // default size
    private final String defaultSortOrder = "DESC";     // default sort order, Ascending or Descending



    private final AuditLogRepository auditLogRepository;
    private final AuditLogService auditLogService;
    private final PermissionService permissionService;
    private final JWTService jwtService;

    private final HttpServletRequest httpRequest;

    public AuditLogController(AuditLogRepository auditLogRepository, AuditLogService auditLogService, PermissionService permissionService, JWTService jwtService, HttpServletRequest httpRequest) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogService = auditLogService;
        this.permissionService = permissionService;
        this.jwtService = jwtService;
        this.httpRequest = httpRequest;
    }

    @PostMapping("/fetch")
    public ResponseEntity<ResponseDto<Page<AuditLogEntity>>> searchAuditLogs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = defaultSortOrder) String sortOrder,
            @RequestParam(defaultValue = defaultPage) int page,
            @RequestParam(defaultValue = defaultSize) int size
    ) {


        permissionService.checkPermission(httpRequest, "ADMIN_GET_AUDIT_LOGS", jwtService);


        return  ResponseEntity.status(HttpStatus.OK).body(auditLogService
                .getAuditLogs(search, merchantOrgId, sortBy, sortOrder, startDate, endDate, page, size ));
    }


    @PostMapping("/merchant/fetch")
    public ResponseEntity<ResponseDto<Page<AuditLogEntity>>>  searchMerchantAuditLogs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = defaultSortOrder) String sortOrder,
            @RequestParam(defaultValue = defaultPage) int page,
            @RequestParam(defaultValue = defaultSize) int size
    ) {

        permissionService.checkPermission(httpRequest, "MERCHANT_GET_AUDIT_LOGS", jwtService);


        return  ResponseEntity.status(HttpStatus.OK).body(auditLogService
                .getAuditLogs(search, merchantOrgId, sortBy, sortOrder, startDate, endDate, page, size ));
    }

}
