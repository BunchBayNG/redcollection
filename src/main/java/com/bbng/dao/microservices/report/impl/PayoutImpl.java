package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.PayoutSpecification;
import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.entity.PayoutEntity;
import com.bbng.dao.microservices.report.repository.PayoutRepository;
import com.bbng.dao.microservices.report.service.PayoutService;
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
public class PayoutImpl implements PayoutService {

    private final PayoutRepository payoutRepository;

    public PayoutImpl(PayoutRepository payoutRepository) {
        this.payoutRepository = payoutRepository;
    }


    @Override
    public ResponseDto<Page<PayoutEntity>>  getPayouts(String search, String merchantOrgId, String status,
                                                       String sortBy, String sortOrder, LocalDate startDate,
                                                       LocalDate endDate, int page, int size) {
        Specification<PayoutEntity> spec =
                PayoutSpecification.getPayouts(search, merchantOrgId, status, startDate, endDate);

        Pageable pageable = getPageable(sortBy, sortOrder, page, size);

        Page<PayoutEntity> response = payoutRepository.findAll(spec, pageable);

        return ResponseDto.<Page<PayoutEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Payouts fetched successfully")
                .data(response)
                .build();
    }

    @Override
    public byte[] exportToCsv(String merchantOrgId, LocalDate startDate, LocalDate endDate) {


        Specification<PayoutEntity> spec = PayoutSpecification.getPayouts("", merchantOrgId, "", startDate, endDate);


        List<PayoutEntity> settlements = payoutRepository.findAll(spec);
        StringBuilder csv = new StringBuilder();

        // Header row
        csv.append("Merchant ID,Merchant Name,Amount,Payout Ref,Transaction Ref,Reference,Source Acct,Destination Acct,Status,Timestamp\n");

        // Data rows
        for (PayoutEntity stm : settlements) {
            csv.append(stm.getMerchantOrgId()).append(",");
            csv.append(stm.getMerchantName()).append(",");
            csv.append(stm.getAmount()).append(",");
            csv.append(stm.getPayoutRef()).append(",");
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

        Specification<PayoutEntity> spec = PayoutSpecification.getPayouts("", merchantOrgId, "", startDate, endDate);


        List<PayoutEntity> settlements = payoutRepository.findAll(spec);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA);

        document.add(new Paragraph("Payout Report", headerFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 4, 2, 2, 4});

        // Headers
        Stream.of("Merchant ID", "Merchant Name","Amount","Payout Ref", "Transaction Ref",
                        "Reference","Source Acct ","Destination Acct","Status","Timestamp")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(title, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Data rows
        for (PayoutEntity stm : settlements) {
            table.addCell(new PdfPCell(new Phrase(stm.getMerchantOrgId(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getMerchantName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getAmount().toString(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(stm.getPayoutRef(), bodyFont)));
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

        Specification<PayoutEntity> spec = PayoutSpecification.getPayouts("", merchantOrgId, "", startDate, endDate);


        List<PayoutEntity> settlements = payoutRepository.findAll(spec);


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Settlements");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] headers = {"Merchant ID", "Merchant Name","Amount","Payout Ref", "Transaction Ref",
                "Reference","Source Acct ","Destination Acct","Status","Timestamp"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (PayoutEntity stm : settlements) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(stm.getMerchantOrgId());
            row.createCell(1).setCellValue(stm.getMerchantName());
            row.createCell(2).setCellValue(stm.getAmount().doubleValue());
            row.createCell(3).setCellValue(stm.getPayoutRef());
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



    public  ResponseDto<AnalyticsCountSummaryDTO> getPayoutCountSummary(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        long total = payoutRepository.countByCreatedAtBetween(merchantOrgId, startDate, endDate);
        long success = payoutRepository.countByStatusAndCreatedAtBetween(merchantOrgId,"SUCCESS", startDate, endDate);
        long pending = payoutRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "PENDING", startDate, endDate);
        long failed = payoutRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "FAILED", startDate, endDate);

        AnalyticsCountSummaryDTO response = new AnalyticsCountSummaryDTO(total, success, pending, failed);

        return ResponseDto.<AnalyticsCountSummaryDTO>builder()
                .statusCode(200)
                .status(true)
                .message("Payouts Count fetched successfully")
                .data(response)
                .build();
    }

    public  ResponseDto<BigDecimal>   getSuccessfulPayoutVolume(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal response =  payoutRepository.sumAmountByStatus(merchantOrgId,"SUCCESS", startDate, endDate);
        return ResponseDto.<BigDecimal>builder()
                .statusCode(200)
                .status(true)
                .message("Successful Payouts Volume fetched successfully")
                .data(response)
                .build();
    }

    public ResponseDto<Double>  getSuccessfulPayoutRate(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        long total = payoutRepository.countByCreatedAtBetween(merchantOrgId, startDate, endDate);
        long success = payoutRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "SUCCESS", startDate, endDate);

        Double response =  total == 0 ? 0 : (double) success / total * 100;
        return ResponseDto.<Double>builder()
                .statusCode(200)
                .status(true)
                .message("Successful Payouts Rate fetched successfully")
                .data(response)
                .build();
    }

    public  ResponseDto<List<ChartPointDTO>> getSuccessfulPayoutVolumeChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult = payoutRepository.groupSuccessfulPayoutVolumeByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> response = rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();

        return ResponseDto.<List<ChartPointDTO>>builder()
                .statusCode(200)
                .status(true)
                .message("Successful Payouts Volume Chart  fetched successfully")
                .data(response)
                .build();
    }



    public  ResponseDto< List<ChartPointDTO>> getSuccessfulPayoutCountChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult =  payoutRepository.groupSuccessfulPayoutCountByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> response = rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();

        return ResponseDto.<List<ChartPointDTO>>builder()
                .statusCode(200)
                .status(true)
                .message("Successful Payouts Count Chart fetched successfully")
                .data(response)
                .build();




    }









}


