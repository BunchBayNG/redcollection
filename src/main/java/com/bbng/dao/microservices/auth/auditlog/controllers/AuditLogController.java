package com.bbng.dao.microservices.auth.auditlog.controllers;

import com.bbng.dao.microservices.auth.auditlog.dto.request.AuditLogFilterRequest;
import com.bbng.dao.microservices.auth.auditlog.entities.AuditLogEntity;
import com.bbng.dao.microservices.auth.auditlog.impl.AuditLogSpecification;
import com.bbng.dao.microservices.auth.auditlog.repository.AuditLogRepository;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiVersion}" + "/audit-log")
public class AuditLogController {


    private final AuditLogRepository auditLogRepository;
    private final PermissionService permissionService;
    private final JWTService jwtService;

    private final HttpServletRequest httpRequest;

    public AuditLogController(AuditLogRepository auditLogRepository, PermissionService permissionService, JWTService jwtService, HttpServletRequest httpRequest) {
        this.auditLogRepository = auditLogRepository;
        this.permissionService = permissionService;
        this.jwtService = jwtService;
        this.httpRequest = httpRequest;
    }

    @PostMapping("/search")
    public ResponseEntity<Page<AuditLogEntity>> searchAuditLogs(@RequestBody AuditLogFilterRequest request) {


        permissionService.checkPermission(httpRequest, "ADMIN_GET_AUDIT_LOGS", jwtService);


        Specification<AuditLogEntity> spec = AuditLogSpecification.getLogs(request);
        Pageable pageable = getPageable(request);

        Page<AuditLogEntity> page = auditLogRepository.findAll(spec, pageable);
        return ResponseEntity.ok(page);
    }


    @PostMapping("/merchant/search")
    public ResponseEntity<Page<AuditLogEntity>> searchMerchantAuditLogs(@RequestBody AuditLogFilterRequest request) {

        permissionService.checkPermission(httpRequest, "MERCHANT_GET_AUDIT_LOGS", jwtService);

        Specification<AuditLogEntity> spec = AuditLogSpecification.getLogs(request);
        Pageable pageable = getPageable(request);

        Page<AuditLogEntity> page = auditLogRepository.findAll(spec, pageable);
        return ResponseEntity.ok(page);
    }

    private Pageable getPageable(AuditLogFilterRequest request) {
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "time";
        String sortOrder = request.getSortOrder() != null ? request.getSortOrder().toUpperCase() : "DESC";

        Sort sort = sortOrder.equals("ASC") ?
                Sort.by(Sort.Direction.ASC, sortBy) :
                Sort.by(Sort.Direction.DESC, sortBy);

        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }
}
