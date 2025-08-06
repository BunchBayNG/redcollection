package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.VnubanSpecification;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.repository.VnubanRepository;
import com.bbng.dao.microservices.report.service.VnubanService;
import com.bbng.dao.microservices.vacctgen.entity.ProvisionedAccount;
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
public class VnubanImpl implements VnubanService {

    private final VnubanRepository vnubanRepository;

    public VnubanImpl(VnubanRepository vnubanRepository) {
        this.vnubanRepository = vnubanRepository;
    }



    @Override
    public ResponseDto<Page<ProvisionedAccount>>  getVnubans(String search, String merchantOrgId, String status,
                                                             String sortBy, String sortOrder, LocalDate startDate,
                                                             LocalDate endDate, int page, int size) {
        Specification<ProvisionedAccount> spec =
                VnubanSpecification.getVnubans(search, merchantOrgId, status, startDate, endDate);

        Pageable pageable = getPageable(sortBy, sortOrder, page, size);

        Page<ProvisionedAccount> response = vnubanRepository.findAll(spec, pageable);

        return ResponseDto.<Page<ProvisionedAccount>>builder()
                .statusCode(200)
                .status(true)
                .message("vNUBANs fetched successfully")
                .data(response)
                .build();
    }
    @Override
    public byte[] exportToCsv(String merchantOrgId, LocalDate startDate, LocalDate endDate) {


        Specification<ProvisionedAccount> spec = VnubanSpecification.getVnubans("", merchantOrgId, "", startDate, endDate);


        List<ProvisionedAccount> vnubans = vnubanRepository.findAll(spec);
        StringBuilder csv = new StringBuilder();

        // Header row
        csv.append("Merchant Org ID,Merchant Name,Amount,BatchRef,Initiator Ref,Acct Name,Acct No,Acct Email,Acct MSISDN,Product Type,Mode,Status,Timestamp\n");

   
        // Data rows
        for (ProvisionedAccount va : vnubans) {
            csv.append(va.getMerchantOrgId()).append(",");
            csv.append(va.getMerchantName()).append(",");
            csv.append(va.getAmount()).append(",");
            csv.append(va.getBatchRef()).append(",");
            csv.append(va.getInitiatorRef()).append(",");
            csv.append(va.getAccountName()).append(",");
            csv.append(va.getAccountNo()).append(",");
            csv.append(va.getAccountEmail()).append(",");
            csv.append(va.getAccountMsisdn()).append(",");
            csv.append(va.getProductType()).append(",");
            csv.append(va.getMode().name()).append(",");
            csv.append(va.getStatus().name()).append(",");
            csv.append(va.getProvisionDate()).append("\n");
        }


        return csv.toString().getBytes();
    }

    @Override
    public byte[] exportToPdf(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<ProvisionedAccount> spec = VnubanSpecification.getVnubans("", merchantOrgId, "", startDate, endDate);


        List<ProvisionedAccount> vnubans = vnubanRepository.findAll(spec);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA);

        document.add(new Paragraph("Virtual Account Number Report", headerFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 4, 2, 2, 4});

        // Headers
        Stream.of("Merchant Org ID", "Merchant Name","Amount","BatchRef","Initiator Ref","Acct Name", "Acct No","Acct Email","Acct MSISDN","Product Type","Mode","Status","Timestamp")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(title, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Data rows
        for (ProvisionedAccount va : vnubans) {
            table.addCell(new PdfPCell(new Phrase(va.getMerchantOrgId(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getMerchantName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getAmount().toString(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getBatchRef(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getInitiatorRef(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getAccountName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getAccountNo(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getAccountEmail(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getAccountMsisdn(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getProductType(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getMode().name(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getStatus().name(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(va.getProvisionDate().toString(), bodyFont)));
        }



        document.add(table);
        document.close();
        return out.toByteArray();
    }


    @Override
    public byte[] exportToExcel(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<ProvisionedAccount> spec = VnubanSpecification.getVnubans("", merchantOrgId, "", startDate, endDate);


        List<ProvisionedAccount> vnubans = vnubanRepository.findAll(spec);


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Virtual Account Numbers");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] headers = {"Merchant Org ID", "Merchant Name","Amount","BatchRef","Initiator Ref","Acct Name", "Acct No","Acct Email","Acct MSISDN","Product Type","Mode","Status","Timestamp"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (ProvisionedAccount va : vnubans) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(va.getMerchantOrgId());
            row.createCell(1).setCellValue(va.getMerchantName());
            row.createCell(2).setCellValue(va.getAmount().doubleValue());
            row.createCell(3).setCellValue(va.getBatchRef());
            row.createCell(4).setCellValue(va.getInitiatorRef());
            row.createCell(5).setCellValue(va.getAccountName());
            row.createCell(6).setCellValue(va.getAccountNo());
            row.createCell(7).setCellValue(va.getAccountEmail());
            row.createCell(8).setCellValue(va.getAccountMsisdn());
            row.createCell(9).setCellValue(va.getProductType());
            row.createCell(10).setCellValue(va.getMode().name());
            row.createCell(11).setCellValue(va.getStatus().name());
            row.createCell(12).setCellValue(va.getProvisionDate().toString());
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


    public ResponseDto<Long>  getTotalVnubans() {
        Long response =  vnubanRepository.count();

        return ResponseDto.<Long>builder()
                .statusCode(200)
                .status(true)
                .message("Total vNUBANs fetched successfully")
                .data(response)
                .build();
    }


    public ResponseDto<Long>  getTotalDistinctiveVnubans(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        Long response =  vnubanRepository.countByProvisionDate(merchantOrgId, startDate, endDate);

        return ResponseDto.<Long>builder()
                .statusCode(200)
                .status(true)
                .message("Total Distinctive vNUBANs fetched successfully")
                .data(response)
                .build();
    }




    public ResponseDto<Long>  getTotalStaticVnubans(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        Long response = vnubanRepository.countByGeneratedAtBetweenForOpen(merchantOrgId, startDate, endDate);

        return ResponseDto.<Long>builder()
                .statusCode(200)
                .status(true)
                .message("Total Static vNUBANs fetched successfully")
                .data(response)
                .build();
    }

    public ResponseDto<Long> getTotalDynamicVnubans(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        Long response =  vnubanRepository.countByGeneratedAtBetweenForClosed(merchantOrgId, startDate, endDate);

        return ResponseDto.<Long>builder()
                .statusCode(200)
                .status(true)
                .message("Total Dynamic vNUBANs fetched successfully")
                .data(response)
                .build();
    }

    public   ResponseDto<List<ChartPointDTO>>  getGeneratedVnubansChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult =  vnubanRepository.groupGeneratedVnubansByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> response  = rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();

        return ResponseDto.<List<ChartPointDTO>>builder()
                .statusCode(200)
                .status(true)
                .message("Total vNUBANs fetched successfully")
                .data(response)
                .build();
    }







}
