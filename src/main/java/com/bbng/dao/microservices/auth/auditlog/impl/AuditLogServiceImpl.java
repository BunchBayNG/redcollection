package com.bbng.dao.microservices.auth.auditlog.impl;


import com.bbng.dao.microservices.auth.auditlog.dto.request.AuditLogRequestDto;
import com.bbng.dao.microservices.auth.auditlog.dto.response.AuditLogResponseDto;
import com.bbng.dao.microservices.auth.auditlog.entities.AuditLogEntity;
import com.bbng.dao.microservices.auth.auditlog.repository.AuditLogRepository;
import com.bbng.dao.microservices.auth.auditlog.service.AuditLogService;
import com.bbng.dao.util.exceptions.customExceptions.BadRequestException;
import com.bbng.dao.util.response.PagedResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
//    private final AuditFilterSpec auditFilterSpec;

    @Override
    public void registerLogToAudit(AuditLogRequestDto auditLogRequestDto) {

        AuditLogEntity auditLog =
                AuditLogEntity.builder()
                        .userId(auditLogRequestDto.getUserId())
                        .event(auditLogRequestDto.getEvent())
                        .merchantName(auditLogRequestDto.getMerchantName())
                        .userName(auditLogRequestDto.getUserName())
                        .email(auditLogRequestDto.getEmail())
                        .userRole(auditLogRequestDto.getUserRole())
                        .userIpAddress(auditLogRequestDto.getUserIpAddress())
                        .description(auditLogRequestDto.getDescription())
                        .build();

        ///AuditLogEntity savedEntity = auditLogRepository.save(auditLog);
        auditLogRepository.save(auditLog);
    }


//    @Override
//    public PagedResponseDto<AuditLogResponseDto> findAllLogs(String id, String merchantName, String merchantId, String userId, String userName, String event, String userType, String dateBegin, String dateEnd, int pageNo, int pageSize) {
//
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, "dateTimeStamp"));
//
//        Page<AuditLogEntity> responsePage = auditLogRepository.findAll(auditFilterSpec.filterTransactionData(
//                Optional.ofNullable((dateBegin == null) ? null : validateDate(dateBegin)),
//                Optional.ofNullable((dateEnd == null) ? null : validateDate(dateBegin)),
//                Optional.ofNullable(id),
//                Optional.ofNullable(userId),
//                Optional.ofNullable(userName),
//                Optional.ofNullable(merchantId),
//                Optional.ofNullable(merchantName),
//                Optional.ofNullable(userType)
//        ), pageable);
//
//
//        Page<AuditLogResponseDto> responses = new PageImpl<>(
//                responsePage.getContent().stream().map(this::mapToAuditLogResponseDto).toList(), pageable, responsePage.getTotalElements());
//
//        return PagedResponseDto.<AuditLogResponseDto>builder()
//                .statusCode(200)
//                .status(true)
//                .message("Fetch all logs")
//                .currentPage(responsePage.getNumber())
//                .itemsPerPage(responses.getSize())
//                .totalItems(responsePage.getTotalElements())
//                .totalPages(responses.getTotalPages())
//                .items(responses.getContent())
//                .build();
//    }
//
//    private Instant validateDate(String date) {
//        if (date == null || date.isEmpty()) {
//            throw new BadRequestException("Date cannot be null");
//        }
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate localDate = LocalDate.parse(date, formatter);
//
//            // Convert LocalDate to Instant at the start of the day in the system default time zone
//            return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
//        } catch (Exception e) {
//            throw new BadRequestException("Date format is incorrect. Format should be yyyy-MM-dd");
//        }
//    }
//
//    private AuditLogResponseDto mapToAuditLogResponseDto(AuditLogEntity auditLog) {
//        return AuditLogResponseDto.builder()
//                .id(auditLog.getId())
//                .event(auditLog.getEvent())
//                .userId(auditLog.getUserId())
//                .userName(auditLog.getUserName())
//                .merchantId(auditLog.getMerchantId())
//                .merchantName(auditLog.getMerchantName())
//                .userType(auditLog.getUserType())
//                .dateTimeStamp(auditLog.getDateTimeStamp())
//                .isDeleted(auditLog.isDeleted())
//                .succeeded(auditLog.isSucceeded())
//                .build();
//    }
}
