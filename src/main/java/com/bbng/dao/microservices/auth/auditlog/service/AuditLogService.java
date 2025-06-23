package com.bbng.dao.microservices.auth.auditlog.service;


import com.bbng.dao.microservices.auth.auditlog.dto.request.AuditLogRequestDto;
import com.bbng.dao.microservices.auth.auditlog.dto.response.AuditLogResponseDto;
import com.bbng.dao.util.response.PagedResponseDto;

public interface AuditLogService {

    void registerLogToAudit(AuditLogRequestDto auditLogRequestDto);

    PagedResponseDto<AuditLogResponseDto> findAllLogs(String id, String merchantName, String merchantId, String userId, String userName, String event, String userType, String dateBegin, String dateEnd, int pageNo, int pageSize);
}
