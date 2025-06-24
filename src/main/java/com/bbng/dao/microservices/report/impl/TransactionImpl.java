package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.auth.organization.entity.SystemConfigEntity;
import com.bbng.dao.microservices.auth.organization.service.ConfigService;
import com.bbng.dao.microservices.report.config.TransactionSpecification;
import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.dto.TopMerchantDTO;
import com.bbng.dao.microservices.report.dto.TransactionRequestDTO;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.microservices.report.repository.TransactionRepository;
import com.bbng.dao.microservices.report.service.SettlementService;
import com.bbng.dao.microservices.report.service.TransactionService;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
import java.util.Random;


@Service
public class TransactionImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ConfigService configService;
    private final SettlementService settlementService;


    public TransactionImpl(TransactionRepository transactionRepository, ConfigService configService,
                           SettlementService settlementService) {
        this.transactionRepository = transactionRepository;
        this.configService = configService;
        this.settlementService = settlementService;
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
        BigDecimal vat = request.getAmount().multiply(vatRate);

        // Create Settlements

        settlementService.initiateSettlement(vat, request.getMerchantName(), request.getMerchantOrgId(), request.getVnuban(),
                request.getDestinationAccountNumber(), "SUCCESS", request.getTransactionId(), request.getReference(), "VAT");

        settlementService.initiateSettlement(adminSplit, request.getMerchantName(), request.getMerchantOrgId(), request.getVnuban(),
                String.valueOf(config.getAdminAccountNo()), "SUCCESS",  request.getTransactionId(),  request.getReference(), "ADMIN");

        settlementService.initiateSettlement(platformSplit, request.getMerchantName(), request.getMerchantOrgId(), request.getVnuban(),
                String.valueOf(config.getAdminAccountNo()), "SUCCESS",  request.getTransactionId(),  request.getReference(), "PLATFORM");


        return ResponseDto.<TransactionEntity>builder()
                .statusCode(200)
                .status(true)
                .message("Transactions fetched successfully")
                .data(txn)
                .build();
    }

    private static TransactionEntity getTransactionEntity(TransactionRequestDTO request) {
        TransactionEntity txn = new TransactionEntity();

        txn.setMerchantName(request.getMerchantName());
        txn.setTransactionId(request.getTransactionId());
        txn.setAmount(request.getAmount());
        txn.setTransactionId(request.getTransactionId());
        txn.setMerchantOrgId(request.getMerchantOrgId());
        txn.setVnuban(request.getVnuban());
        txn.setStatus(request.getStatus());
        txn.setSessionId(request.getSessionId());
        txn.setReference(request.getReference());
        txn.setWebhookStatus("200");
        txn.setTransactionType(request.getTransactionType());
        txn.setDestinationAccountNumber(request.getDestinationAccountNumber());
        txn.setDestinationAccountName(request.getDestinationAccountName());
        txn.setIpAddress("");
        txn.setDeviceName("");
        txn.setProcessingTime(1L);
        return txn;
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


    public ResponseDto<Long> getTotalMerchantCount(LocalDateTime startDate, LocalDateTime endDate) {
        Long response =  transactionRepository.countDistinctMerchantOrgId(startDate, endDate);

        return ResponseDto.<Long>builder()
                .statusCode(200)
                .status(true)
                .message("Total Merchants fetched successfully")
                .data(response)
                .build();
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



}
