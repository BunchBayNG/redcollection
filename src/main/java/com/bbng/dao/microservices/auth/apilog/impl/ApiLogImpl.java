package com.bbng.dao.microservices.auth.apilog.impl;

import com.bbng.dao.microservices.auth.apilog.entity.ApiLogEntity;
import com.bbng.dao.microservices.auth.apilog.repository.ApiLogRepository;
import com.bbng.dao.microservices.report.config.TransactionSpecification;
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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;


@Service
public class ApiLogImpl {


    private final ApiLogRepository apiLogRepository;

    public ApiLogImpl(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }




    public ResponseDto<Page<ApiLogEntity>> getApiLogs(String search, String merchantPrefix, String status,
                                                                String sortBy, String sortOrder, LocalDate startDate,
                                                                LocalDate endDate, int page, int size) {
        Specification<ApiLogEntity> spec =
                ApiLogSpecification.getLogs(search, merchantPrefix, status, startDate, endDate);

        Pageable pageable = getPageable(sortBy, sortOrder, page, size);

        Page<ApiLogEntity> response = apiLogRepository.findAll(spec, pageable);

        return ResponseDto.<Page<ApiLogEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Api logs fetched successfully")
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








    public byte[] exportToCsv(String merchantPrefix, LocalDate startDate, LocalDate endDate) {


        Specification<ApiLogEntity> spec = ApiLogSpecification.getLogs("", merchantPrefix, "", startDate, endDate);


        List<ApiLogEntity> apiLogs = apiLogRepository.findAll(spec);
        StringBuilder csv = new StringBuilder();

        // Header row
        csv.append("Merchant Prefix ID,Request Time Stamp,Response Time Stamp,Response Status,Service,Timestamp\n");


        // Data rows
        for (ApiLogEntity api : apiLogs) {
            csv.append(api.getMerchantPrefix()).append(",");
            csv.append(api.getRequestTimestamp()).append(",");
            csv.append(api.getResponseTimestamp()).append(",");
            csv.append(api.getResponseStatus()).append(",");
            csv.append(api.getService()).append(",");
            csv.append(api.getCreatedAt()).append(",");
        }


        return csv.toString().getBytes();
    }

    
    public byte[] exportToPdf(String merchantPrefix, LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<ApiLogEntity> spec = ApiLogSpecification.getLogs("", merchantPrefix, "", startDate, endDate);


        List<ApiLogEntity> apiLogs = apiLogRepository.findAll(spec);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA);

        document.add(new Paragraph("API Log Report", headerFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 4, 2, 2, 4});

        // Headers
        Stream.of("Merchant Prefix ID", "Request Time Stamp","Response Time Stamp","Response Status","Service","Timestamp")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(title, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Data rows
        for (ApiLogEntity api : apiLogs) {
            table.addCell(new PdfPCell(new Phrase(api.getMerchantPrefix(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(api.getRequestTimestamp().toString(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(api.getResponseTimestamp().toString(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(api.getResponseStatus()), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(api.getService(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(api.getCreatedAt().toString(), bodyFont)));
            

        }



        document.add(table);
        document.close();
        return out.toByteArray();
    }


    
    public byte[] exportToExcel(String merchantPrefix, LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<ApiLogEntity> spec = ApiLogSpecification.getLogs("", merchantPrefix, "", startDate, endDate);


        List<ApiLogEntity> apiLogs = apiLogRepository.findAll(spec);


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("API Logs");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        
                
        String[] headers = {"Merchant Prefix ID", "Request Time Stamp","Response Time Stamp","Response Status","Service","Timestamp"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (ApiLogEntity api : apiLogs) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(api.getMerchantPrefix());
            row.createCell(1).setCellValue(api.getRequestTimestamp());
            row.createCell(2).setCellValue(api.getResponseTimestamp());
            row.createCell(3).setCellValue(api.getResponseStatus());
            row.createCell(4).setCellValue(api.getService());
            row.createCell(5).setCellValue(api.getCreatedAt().toString());
            
        }


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
    
    
}
