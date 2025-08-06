package com.bbng.dao.microservices.auth.auditlog.impl;




import com.bbng.dao.microservices.auth.auditlog.dto.request.AuditLogRequestDto;
import com.bbng.dao.microservices.auth.auditlog.entities.AuditLogEntity;
import com.bbng.dao.microservices.auth.auditlog.repository.AuditLogRepository;
import com.bbng.dao.microservices.auth.auditlog.service.AuditLogService;
import com.bbng.dao.util.response.ResponseDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

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

    public ResponseDto<Page<AuditLogEntity>> getAuditLogs(String search, String merchantPrefix,
                                                      String sortBy, String sortOrder, LocalDate startDate,
                                                      LocalDate endDate, int page, int size) {
        Specification<AuditLogEntity> spec =
                AuditLogSpecification.getLogs(search, merchantPrefix, startDate, endDate);

        Pageable pageable = getPageable(sortBy, sortOrder, page, size);

        Page<AuditLogEntity> response = auditLogRepository.findAll(spec, pageable);

        return ResponseDto.<Page<AuditLogEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Audit logs fetched successfully")
                .data(response)
                .build();
    }


    private Pageable getPageable(String sortBy, String sortOrder, int page, int size) {
        String  defaultSortBy = sortBy != null ? sortBy : "createdAt";
        String defaultSortOrder = sortOrder != null ? sortOrder.toUpperCase() : "DESC";

        Sort sort = switch (defaultSortOrder) {
            case "ASC" -> Sort.by(Sort.Direction.ASC, defaultSortBy);
            case "DESC" -> Sort.by(Sort.Direction.DESC, defaultSortBy);
            case "ACTIVE_FIRST" -> Sort.by(Sort.Order.desc("status"));
            case "INACTIVE_FIRST" -> Sort.by(Sort.Order.asc("status"));
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };

        return PageRequest.of(page, size, sort);
    }



    @Override
    public byte[] exportToCsv(String merchantOrgId, LocalDate startDate, LocalDate endDate) {


        Specification<AuditLogEntity> spec = AuditLogSpecification.getLogs("", merchantOrgId, startDate, endDate);


        List<AuditLogEntity> auditLogs = auditLogRepository.findAll(spec);
        StringBuilder csv = new StringBuilder();

        // Header row
        csv.append( "User ID,User Name,Email,User Role,IP Address,Merchant Name,Merchant Organization,Merchant Org Id,Event,User Type,Description,Timestamp\n");



        // Data rows
        for (AuditLogEntity audit : auditLogs) {
            csv.append(audit.getUserId()).append(",");
            csv.append(audit.getUserName()).append(",");
            csv.append(audit.getEmail()).append(",");
            csv.append(audit.getUserRole()).append(",");
            csv.append(audit.getUserIpAddress()).append(",");
            csv.append(audit.getMerchantName()).append(",");
            csv.append(audit.getMerchantOrganization()).append(",");
            csv.append(audit.getMerchantOrgId()).append(",");
            csv.append(audit.getEvent()).append(",");
            csv.append(audit.getUserType()).append(",");
            csv.append(audit.getDescription()).append(",");
            csv.append(audit.getCreatedAt()).append(",");


        }


        return csv.toString().getBytes();
    }

    @Override
    public byte[] exportToPdf(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<AuditLogEntity> spec = AuditLogSpecification.getLogs("", merchantOrgId, startDate, endDate);


        List<AuditLogEntity> auditLogs = auditLogRepository.findAll(spec);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA);

        document.add(new Paragraph("Audit Log Report", headerFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 4, 2, 2, 4});

        // Headers
        Stream.of("User ID","User Name","Email","User Role","IP Address","Merchant Name","Merchant Organization",
                "Merchant Org Id","Event","User Type","Description","Timestamp")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(title, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Data rows
        for (AuditLogEntity audit : auditLogs) {
            table.addCell(new PdfPCell(new Phrase(audit.getUserId(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getUserName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getEmail().toString(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getUserRole(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getUserIpAddress(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getMerchantName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getMerchantOrganization(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getMerchantOrgId(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getEvent(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getUserType(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getDescription(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(audit.getCreatedAt().toString(), bodyFont)));

        }



        document.add(table);
        document.close();
        return out.toByteArray();
    }


    @Override
    public byte[] exportToExcel(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<AuditLogEntity> spec = AuditLogSpecification.getLogs("", merchantOrgId, startDate, endDate);


        List<AuditLogEntity> auditLogs = auditLogRepository.findAll(spec);


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Audit Logs");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] headers = {"User ID", "User Name","Email","User Role","IP Address","Merchant Name", "Merchant Organization",
                "Merchant Org Id", "Event", "User Type","Description","Timestamp"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (AuditLogEntity audit : auditLogs) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(audit.getUserId());
            row.createCell(1).setCellValue(audit.getUserName());
            row.createCell(2).setCellValue(audit.getEmail());
            row.createCell(3).setCellValue(audit.getUserRole());
            row.createCell(4).setCellValue(audit.getUserIpAddress());
            row.createCell(5).setCellValue(audit.getMerchantName());
            row.createCell(6).setCellValue(audit.getMerchantOrganization());
            row.createCell(7).setCellValue(audit.getMerchantOrgId());
            row.createCell(8).setCellValue(audit.getEvent());
            row.createCell(9).setCellValue(audit.getUserType());
            row.createCell(10).setCellValue(audit.getDescription());
            row.createCell(11).setCellValue(audit.getCreatedAt().toString());

        }


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

}
