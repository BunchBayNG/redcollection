package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.SettlementSpecification;
import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.entity.SettlementEntity;
import com.bbng.dao.microservices.report.repository.SettlementRepository;
import com.bbng.dao.microservices.report.service.SettlementService;
import com.bbng.dao.util.response.ResponseDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SettlementImpl implements SettlementService {

    private final SettlementRepository settlementRepository;

    public SettlementImpl(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    @Override
    public ResponseDto<Page<SettlementEntity>>  getSettlements(String search, String merchantOrgId, String status,
                                                               String sortBy, String sortOrder, LocalDate startDate,
                                                               LocalDate endDate, int page, int size) {
        Specification<SettlementEntity> spec =
                SettlementSpecification.getSettlements(search, merchantOrgId, status, startDate, endDate);

        Pageable pageable = getPageable(sortBy, sortOrder, page, size);

        Page<SettlementEntity> response = settlementRepository.findAll(spec, pageable);

        return ResponseDto.<Page<SettlementEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Settlements fetched successfully")
                .data(response)
                .build();
    }

    @Override
    public byte[] exportToCsv(String merchantOrgId, LocalDate startDate, LocalDate endDate) {


        Specification<SettlementEntity> spec = SettlementSpecification.getSettlements("", merchantOrgId, "", startDate, endDate);


        List<SettlementEntity> settlements = settlementRepository.findAll(spec);
        StringBuilder csv = new StringBuilder();

        // Header row
        csv.append("Merchant ID,Merchant Name,Amount,Settlement Ref,Transaction Ref,Reference,Source Acct,Destination Acct,Status,Timestamp\n");

        // Data rows
        for (SettlementEntity stm : settlements) {
            csv.append(stm.getMerchantOrgId()).append(",");
            csv.append(stm.getMerchantName()).append(",");
            csv.append(stm.getAmount()).append(",");
            csv.append(stm.getSettlementRef()).append(",");
            csv.append(stm.getTransactionRef()).append(",");
            csv.append(stm.getReference()).append(",");
            csv.append(stm.getSourceAccount()).append(",");
            csv.append(stm.getDestinationAccount()).append(",");
            csv.append(stm.getStatus()).append(",");
            csv.append(stm.getCreatedAt()).append("\n");
        }

        return csv.toString().getBytes();
    }

    @Override
    public byte[] exportToPdf(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<SettlementEntity> spec = SettlementSpecification.getSettlements("", merchantOrgId, "", startDate, endDate);


        List<SettlementEntity> settlements = settlementRepository.findAll(spec);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA);

        document.add(new Paragraph("Settlement Report", headerFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 4, 2, 2, 4});

        // Headers
        Stream.of("Merchant ID", "Merchant Name","Amount","Settlement Ref", "Transaction Ref",
                        "Reference","Source Acct ","Destination Acct","Status","Timestamp")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(title, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Data rows
        for (SettlementEntity stm : settlements) {
            table.addCell(new PdfPCell(new Phrase(stm.getMerchantOrgId(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getMerchantName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getAmount().toString(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getSettlementRef(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getTransactionRef(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getReference(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getSourceAccount(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getDestinationAccount(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getStatus(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getCreatedAt().toString(), bodyFont)));
        }





        document.add(table);
        document.close();
        return out.toByteArray();
    }


    @Override
    public byte[] exportToExcel(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<SettlementEntity> spec = SettlementSpecification.getSettlements("", merchantOrgId, "", startDate, endDate);


        List<SettlementEntity> settlements = settlementRepository.findAll(spec);


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Settlements");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] headers = {"Merchant ID", "Merchant Name","Amount","Settlement Ref", "Transaction Ref",
                "Reference","Source Acct ","Destination Acct","Status","Timestamp"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (SettlementEntity stm : settlements) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(stm.getMerchantOrgId());
            row.createCell(1).setCellValue(stm.getMerchantName());
            row.createCell(2).setCellValue(stm.getAmount().doubleValue());
            row.createCell(3).setCellValue(stm.getSettlementRef());
            row.createCell(4).setCellValue(stm.getTransactionRef());
            row.createCell(5).setCellValue(stm.getReference());;
            row.createCell(6).setCellValue(stm.getSourceAccount());
            row.createCell(7).setCellValue(stm.getDestinationAccount());
            row.createCell(8).setCellValue(stm.getStatus());
            row.createCell(9).setCellValue(stm.getCreatedAt().toString());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }



    public void initiateSettlement(BigDecimal amount, String merchantName, String merchantOrgId,
                                   String sourceAccount, String destinationAccount, String status,
                                   String transactionRef, String reference, String settlementRef) {

        SettlementEntity newSettlement = new SettlementEntity();
        newSettlement.setAmount(amount);
        newSettlement.setMerchantName(merchantName);
        newSettlement.setMerchantOrgId(merchantOrgId);
        newSettlement.setSourceAccount(sourceAccount);
        newSettlement.setDestinationAccount(destinationAccount);
        newSettlement.setStatus(status);
        newSettlement.setTransactionRef(transactionRef);
        newSettlement.setReference(reference);
        newSettlement.setSettlementRef(settlementRef);
        settlementRepository.save(newSettlement);
        
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



    public  ResponseDto<AnalyticsCountSummaryDTO>  getSettlementCountSummary(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        long total = settlementRepository.countByCreatedAtBetween(merchantOrgId, startDate, endDate);
        long success = settlementRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "SUCCESS", startDate, endDate);
        long pending = settlementRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "PENDING", startDate, endDate);
        long failed = settlementRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "FAILED", startDate, endDate);

        AnalyticsCountSummaryDTO response = new AnalyticsCountSummaryDTO(total, success, pending, failed);

        return ResponseDto.<AnalyticsCountSummaryDTO>builder()
                .statusCode(200)
                .status(true)
                .message("Settlements count fetched successfully")
                .data(response)
                .build();
    }

    public ResponseDto<BigDecimal>  getSuccessfulSettlementVolume(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {

        return ResponseDto.<BigDecimal>builder()
                .statusCode(200)
                .status(true)
                .message("Successful Settlements Volume fetched successfully")
                .data(settlementRepository.sumAmountByStatus(merchantOrgId, "SUCCESS", startDate, endDate))
                .build();
    }

    public ResponseDto<Double> getSuccessfulSettlementRate(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        long total = settlementRepository.countByCreatedAtBetween(merchantOrgId, startDate, endDate);
        long success = settlementRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "SUCCESS", startDate, endDate);

        Double response = total == 0 ? 0 : (double) success / total * 100;

        return ResponseDto.<Double>builder()
                .statusCode(200)
                .status(true)
                .message("Successful Settlements rate fetched successfully")
                .data(response)
                .build();
    }
/*
//    public List<ChartPointDTO> getSuccessfulSettlementVolumeChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {
//        return settlementRepository.groupSuccessfulSettlementVolumeByPeriod(merchantOrgId, pattern, startDate, endDate);
//    }
//
//    public List<ChartPointDTO> getSuccessfulSettlementCountChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {
//        return settlementRepository.groupSuccessfulSettlementCountByPeriod(merchantOrgId, pattern, startDate, endDate);
//    }

 */


    public ResponseDto<List<ChartPointDTO>> getSuccessfulSettlementVolumeChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult = settlementRepository.groupSuccessfulSettlementVolumeByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> response =  rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();

        return ResponseDto.<List<ChartPointDTO>>builder()
                .statusCode(200)
                .status(true)
                .message("Successful Settlements volume chart fetched successfully")
                .data(response)
                .build();
    }


    public ResponseDto<List<ChartPointDTO>> getSuccessfulSettlementCountChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult =  settlementRepository.groupSuccessfulSettlementCountByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> response =  rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();

        return ResponseDto.<List<ChartPointDTO>>builder()
                .statusCode(200)
                .status(true)
                .message("Successful Settlements count chart fetched successfully")
                .data(response)
                .build();
    }





}
