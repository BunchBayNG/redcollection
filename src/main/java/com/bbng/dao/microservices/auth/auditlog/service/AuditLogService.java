package com.bbng.dao.microservices.auth.auditlog.service;


import com.bbng.dao.microservices.auth.auditlog.dto.request.AuditLogRequestDto;
import com.bbng.dao.microservices.auth.auditlog.dto.response.AuditLogResponseDto;
import com.bbng.dao.microservices.auth.auditlog.entities.AuditLogEntity;
import com.bbng.dao.util.response.PagedResponseDto;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface AuditLogService {

    void registerLogToAudit(AuditLogRequestDto auditLogRequestDto);

  ///  PagedResponseDto<AuditLogResponseDto> findAllLogs(String id, String merchantName, String merchantId, String userId, String userName, String event, String userType, String dateBegin, String dateEnd, int pageNo, int pageSize);


  ResponseDto<Page<AuditLogEntity>> getAuditLogs(String search, String merchantPrefix,
                                                 String sortBy, String sortOrder, LocalDate startDate,
                                                 LocalDate endDate, int page, int size);

    byte[] exportToExcel(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception;
    byte[] exportToPdf(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception;
    byte[] exportToCsv(String merchantOrgId, LocalDate startDate, LocalDate endDate);


}
