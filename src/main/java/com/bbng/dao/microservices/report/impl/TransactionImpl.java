package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.auth.config.entity.SystemConfigEntity;
import com.bbng.dao.microservices.auth.config.service.ConfigService;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.repository.APIKeyRepository;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.organization.utils.GetUserFromToken;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.microservices.report.config.TransactionSpecification;
import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.dto.TopMerchantDTO;
import com.bbng.dao.microservices.report.dto.TransactionRequestDTO;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.microservices.report.repository.TransactionRepository;
import com.bbng.dao.microservices.report.service.SettlementService;
import com.bbng.dao.microservices.report.service.TransactionService;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import com.bbng.dao.util.exceptions.customExceptions.UserNotFoundException;
import com.bbng.dao.util.response.ResponseDto;
import com.lowagie.text.Font;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


@Service
public class TransactionImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ConfigService configService;
    private final SettlementService settlementService;
    private final HttpServletRequest httpRequest;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final APIKeyRepository apiKeyRepository;



    public TransactionImpl(TransactionRepository transactionRepository, ConfigService configService,
                           SettlementService settlementService, HttpServletRequest httpRequest, OrganizationRepository organizationRepository, UserRepository userRepository, APIKeyRepository apiKeyRepository) {
        this.transactionRepository = transactionRepository;
        this.configService = configService;
        this.settlementService = settlementService;
        this.httpRequest = httpRequest;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.apiKeyRepository = apiKeyRepository;
    }


    @Override
    @Transactional
    public ResponseDto<TransactionEntity> createTransaction(TransactionRequestDTO request) {

        TransactionEntity txn = getTransactionEntity(request);

        transactionRepository.save(txn);

        SystemConfigEntity config = configService.getConfig();

        BigDecimal commissionRate = config.getCommissionPercent().divide(BigDecimal.valueOf(100));
        BigDecimal vatRate = config.getVatPercent().divide(BigDecimal.valueOf(100));
        BigDecimal commissionCap = config.getCommissionCap();

        BigDecimal commission = request.getAmount().multiply(commissionRate);
        if (commission.compareTo(commissionCap) > 0) commission = commissionCap;

        BigDecimal adminSplit = commission.multiply(config.getAdminSplitPercent().divide(BigDecimal.valueOf(100)));
        BigDecimal platformSplit = commission.subtract(adminSplit);
        BigDecimal vat = commission.multiply(vatRate);

        // Create Settlements

        settlementService.initiateSettlement(vat, txn.getMerchantName(), txn.getMerchantOrgId(), request.getVnuban(),
                request.getDestinationAccountNumber(), "SUCCESS", txn.getTransactionId(), request.getReference(), "VAT");

        settlementService.initiateSettlement(adminSplit, txn.getMerchantName(), txn.getMerchantOrgId(), request.getVnuban(),
                String.valueOf(config.getAdminAccountNo()), "SUCCESS",  txn.getTransactionId(),  request.getReference(), "ADMIN-UBA");

        settlementService.initiateSettlement(platformSplit, txn.getMerchantName(), txn.getMerchantOrgId(), request.getVnuban(),
                String.valueOf(config.getPlatformAccountNo()), "SUCCESS",  txn.getTransactionId(),  request.getReference(), "PLATFORM-REDTECH");


        return ResponseDto.<TransactionEntity>builder()
                .statusCode(200)
                .status(true)
                .message("Transactions fetched successfully")
                .data(txn)
                .build();
    }

    @Transactional
    public TransactionEntity getTransactionEntity(TransactionRequestDTO request) {

        String transactionId = new ULID().nextULID().substring(20);

        OrganizationEntity org = getOrgForApiKey();
        TransactionEntity txn = new TransactionEntity();

        txn.setMerchantName(org.getOrganizationName());
        txn.setTransactionId(transactionId);
        txn.setAmount(request.getAmount());
        txn.setMerchantOrgId(org.getId());
        txn.setVnuban(request.getVnuban());
        txn.setStatus(request.getStatus());
        txn.setSessionId(request.getSessionId());
        txn.setReference(request.getReference());
        txn.setWebhookStatus("200");
        txn.setTransactionType(request.getTransactionType());
        txn.setDestinationAccountNumber(request.getDestinationAccountNumber());
        txn.setDestinationAccountName(request.getDestinationAccountName());
        txn.setDestinationBankName(request.getDestinationBankName());
        txn.setIpAddress("");
        txn.setProcessingTime(1L);
        return txn;
    }

    public OrganizationEntity getOrgForApiKey() {


        String email = GetUserFromToken.extractUserFromApiKey(httpRequest,apiKeyRepository, userRepository);

        ///log.info("email: {}", email);


        UserEntity user = userRepository.findByUsernameOrEmail(email, email).orElseThrow(() ->
                new UserNotFoundException("Can't find user with the username extracted from token. Is user a paysub user?"));


        return organizationRepository.findOrganizationByMerchantAdminId(user.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Can't find Org with the username extracted from token."));
    }

    @Override
    public ResponseDto<Page<TransactionEntity>>  getTransactions(String search, String merchantOrgId, String status,
                                                                 String sortBy, String sortOrder, LocalDate startDate,
                                                                 LocalDate endDate, int page, int size) {
        Specification<TransactionEntity> spec =
                TransactionSpecification.getTransactions(search, merchantOrgId, status, startDate, endDate);

        Pageable pageable = getPageable(sortBy, sortOrder, page, size);
        
        Page<TransactionEntity> response = transactionRepository.findAll(spec, pageable);

        return ResponseDto.<Page<TransactionEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Transactions fetched successfully")
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



    public  ResponseDto< List<TopMerchantDTO> > getTopMerchantsByVolume(LocalDateTime startDate, LocalDateTime endDate, int topN) {

        List<Object[]> rawResult = transactionRepository.findTopMerchantsByVolume(startDate, endDate, topN);

        List<TopMerchantDTO> response = rawResult.stream()
                .map(row -> new TopMerchantDTO(
                        ((Number) row[0]).longValue(), // merchantOrgId
                        ((String) row[1]), // merchantOrgId
                        (BigDecimal) row[2]            // totalVolume
                ))
                .toList();

        return ResponseDto.<List<TopMerchantDTO>>builder()
                .statusCode(200)
                .status(true)
                .message("Transactions performing Merchants fetched successfully")
                .data(response)
                .build();
    }

    public   ResponseDto<AnalyticsCountSummaryDTO>  getTransactionCountSummary(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        long total = transactionRepository.countByCreatedAtBetween(merchantOrgId, startDate, endDate);
        long success = transactionRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "SUCCESS", startDate, endDate);
        long pending = transactionRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "PENDING", startDate, endDate);
        long failed = transactionRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "FAILED", startDate, endDate);

        AnalyticsCountSummaryDTO response = new AnalyticsCountSummaryDTO(total, success, pending, failed);

        return ResponseDto.<AnalyticsCountSummaryDTO>builder()
                .statusCode(200)
                .status(true)
                .message("Transactions analytics count fetched successfully")
                .data(response)
                .build();
    }

    public  ResponseDto<BigDecimal> getSuccessfulTransactionVolume(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal response = transactionRepository.sumAmountByStatus(merchantOrgId, "SUCCESS", startDate, endDate);
        return ResponseDto.<BigDecimal>builder()
                .statusCode(200)
                .status(true)
                .message("Successful TransactionEntity Volume fetched successfully")
                .data(response)
                .build();


    }

    public ResponseDto<Double> getSuccessfulTransactionRate(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        long total = transactionRepository.countByCreatedAtBetween(merchantOrgId, startDate, endDate);
        long success = transactionRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "SUCCESS", startDate, endDate);

        Double response =  total == 0 ? 0 : (double) success / total * 100;

        return ResponseDto.<Double>builder()
                .statusCode(200)
                .status(true)
                .message("Successful Transactions Rate fetched successfully")
                .data(response)
                .build();
    }

    public ResponseDto<List<ChartPointDTO>> getSuccessfulTransactionVolumeChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult = transactionRepository.groupSuccessfulTransactionVolumeByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> response = rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();

        return ResponseDto.<List<ChartPointDTO>>builder()
                .statusCode(200)
                .status(true)
                .message("Success Transactions Volume Chart fetched successfully")
                .data(response)
                .build();
    }


    public ResponseDto<List<ChartPointDTO>>  getSuccessfulTransactionCountChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult =  transactionRepository.groupSuccessfulTransactionCountByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> response = rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();


        return ResponseDto.<List<ChartPointDTO>>builder()
                .statusCode(200)
                .status(true)
                .message("Success Transactions Count Chart fetched successfully")
                .data(response)
                .build();
    }


    @Override
    public byte[] exportToCsv(String merchantOrgId, LocalDate startDate, LocalDate endDate) {


        Specification<TransactionEntity> spec = TransactionSpecification.getTransactions("", merchantOrgId, "", startDate, endDate);


        List<TransactionEntity> transactions = transactionRepository.findAll(spec);
        StringBuilder csv = new StringBuilder();

        // Header row
        csv.append("Transaction ID,Merchant Name,Amount,VNuban,Reference,WebhookStatus,SessionId,Transaction Type,Dest.Acct No,Dest. Acct Name,Dest. Bank Name,IP Address,Processing Time,Status,Timestamp\n");

        // Data rows
        for (TransactionEntity tx : transactions) {
            csv.append(tx.getTransactionId()).append(",");
            csv.append(tx.getMerchantName()).append(",");
            csv.append(tx.getAmount()).append(",");
            csv.append(tx.getVnuban()).append(",");
            csv.append(tx.getReference()).append(",");
            csv.append(tx.getWebhookStatus()).append(",");
            csv.append(tx.getSessionId()).append(",");
            csv.append(tx.getTransactionType()).append(",");
            csv.append(tx.getDestinationAccountNumber()).append(",");
            csv.append(tx.getDestinationAccountName()).append(",");
            csv.append(tx.getDestinationBankName()).append(",");
            csv.append(tx.getIpAddress()).append(",");
            csv.append(tx.getProcessingTime()).append(",");
            csv.append(tx.getStatus()).append(",");
            csv.append(tx.getCreatedAt()).append("\n");
        }


        return csv.toString().getBytes();
    }

    @Override
    public byte[] exportToPdf(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<TransactionEntity> spec = TransactionSpecification.getTransactions("", merchantOrgId, "", startDate, endDate);


        List<TransactionEntity> transactions = transactionRepository.findAll(spec);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA);

        document.add(new Paragraph("Transaction Report", headerFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 4, 2, 2, 4});

        // Headers
        Stream.of("Transaction ID", "Merchant Name","Amount","VNuban","Reference","WebhookStatus","SessionId","Transaction Type","Dest.Acct No","Dest. Acct Name","Dest. Bank Name","IP Address","Processing Time","Status","Timestamp")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(title, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Data rows
        for (TransactionEntity tx : transactions) {
            table.addCell(new PdfPCell(new Phrase(tx.getTransactionId(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getMerchantName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getAmount().toString(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getVnuban(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getReference(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getWebhookStatus(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getSessionId(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getTransactionType(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getDestinationAccountNumber(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getDestinationAccountName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getDestinationBankName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getIpAddress(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getProcessingTime().toString(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getStatus(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(tx.getCreatedAt().toString(), bodyFont)));
        }


        document.add(table);
        document.close();
        return out.toByteArray();
    }


    @Override
    public byte[] exportToExcel(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<TransactionEntity> spec = TransactionSpecification.getTransactions("", merchantOrgId, "", startDate, endDate);


        List<TransactionEntity> transactions = transactionRepository.findAll(spec);


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row header = sheet.createRow(0);
        String[] headers = {"Transaction ID", "Merchant Name","Amount","VNuban","Reference","WebhookStatus","SessionId","Transaction Type","Dest.Acct No","Dest. Acct Name","Dest. Bank Name","IP Address","Processing Time","Status","Timestamp"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (TransactionEntity tx : transactions) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(tx.getTransactionId());
            row.createCell(1).setCellValue(tx.getMerchantName());
            row.createCell(2).setCellValue(tx.getAmount().doubleValue());
            row.createCell(3).setCellValue(tx.getVnuban());
            row.createCell(4).setCellValue(tx.getReference());
            row.createCell(5).setCellValue(tx.getWebhookStatus());
            row.createCell(6).setCellValue(tx.getSessionId());
            row.createCell(7).setCellValue(tx.getTransactionType());
            row.createCell(8).setCellValue(tx.getDestinationAccountNumber());
            row.createCell(9).setCellValue(tx.getDestinationAccountName());
            row.createCell(10).setCellValue(tx.getDestinationBankName());
            row.createCell(11).setCellValue(tx.getIpAddress());
            row.createCell(12).setCellValue(tx.getProcessingTime());
            row.createCell(13).setCellValue(tx.getStatus());
            row.createCell(14).setCellValue(tx.getCreatedAt().toString());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }




}
