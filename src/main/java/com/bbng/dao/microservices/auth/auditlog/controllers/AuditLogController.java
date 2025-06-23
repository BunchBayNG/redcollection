package com.bbng.dao.microservices.auth.auditlog.controllers;

import com.bbng.dao.microservices.auth.auditlog.dto.response.AuditLogResponseDto;
import com.bbng.dao.microservices.auth.auditlog.service.AuditLogService;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.PermissionService;
import com.bbng.dao.util.response.PagedResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${apiVersion}" + "/audit-Log")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;
    private final PermissionService permissionService;
    private final HttpServletRequest request;
    private final JWTService jwtService;

    @GetMapping("admin/get/audit-logs")
    public ResponseEntity<PagedResponseDto<AuditLogResponseDto>> getLogs(
            @RequestParam(required = false, defaultValue = "1", value = "pageNo")
            @Min(value = 1, message = "Minimum page no is 1") int pageNo,
            @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String merchantId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String event,
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) String dateBegin,
            @RequestParam(required = false) String dateEnd
    ) {

        permissionService.checkPermission(request, "ADMIN_GET_AUDIT_LOGS", jwtService);

        return ResponseEntity.status(HttpStatus.OK).body(auditLogService.findAllLogs(id, merchantName, merchantId, userId, userName, event, userType, dateBegin, dateEnd, pageNo, pageSize));
    }

    @GetMapping("merchant/get/audit-logs")
    public ResponseEntity<PagedResponseDto<AuditLogResponseDto>> getMerchantLogs(
            @RequestParam(required = false, defaultValue = "1", value = "pageNo")
            @Min(value = 1, message = "Minimum page no is 1") int pageNo,
            @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,

            @RequestParam(required = false) String id,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String merchantId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String event,
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) String dateBegin,
            @RequestParam(required = false) String dateEnd
    ) {

        permissionService.checkPermission(request, "MERCHANT_GET_AUDIT_LOGS", jwtService);

        return ResponseEntity.status(HttpStatus.OK).body(auditLogService.findAllLogs(id, merchantName, merchantId, userId, userName, event, userType, dateBegin, dateEnd, pageNo, pageSize));
    }
}
