package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.TransactionSpecification;
import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.dto.TopMerchantDTO;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.microservices.report.repository.TransactionRepository;
import com.bbng.dao.microservices.report.service.TransactionService;
import com.bbng.dao.util.response.ResponseDto;
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


@Service
public class TransactionImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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


    public long getTotalMerchantCount(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.countDistinctMerchantOrgId(startDate, endDate);
    }

    public List<TopMerchantDTO> getTopMerchantsByVolume(LocalDateTime startDate, LocalDateTime endDate, int topN) {

        List<Object[]> rawResult = transactionRepository.findTopMerchantsByVolume(startDate, endDate, topN);

        List<TopMerchantDTO> result = rawResult.stream()
                .map(row -> new TopMerchantDTO(
                        ((Number) row[0]).longValue(), // merchantOrgId
                        ((String) row[1]), // merchantOrgId
                        (BigDecimal) row[2]            // totalVolume
                ))
                .toList();
        return result;
    }

    public AnalyticsCountSummaryDTO getTransactionCountSummary(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        long total = transactionRepository.countByCreatedAtBetween(merchantOrgId, startDate, endDate);
        long success = transactionRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "SUCCESS", startDate, endDate);
        long pending = transactionRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "PENDING", startDate, endDate);
        long failed = transactionRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "FAILED", startDate, endDate);

        return new AnalyticsCountSummaryDTO(total, success, pending, failed);
    }

    public BigDecimal getSuccessfulTransactionVolume(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.sumAmountByStatus(merchantOrgId, "SUCCESS", startDate, endDate);
    }

    public double getSuccessfulTransactionRate(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        long total = transactionRepository.countByCreatedAtBetween(merchantOrgId, startDate, endDate);
        long success = transactionRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "SUCCESS", startDate, endDate);

        return total == 0 ? 0 : (double) success / total * 100;
    }
//
//    public List<ChartPointDTO> getSuccessfulTransactionVolumeChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {
//        return transactionRepository.groupSuccessfulTransactionVolumeByPeriod(merchantOrgId, pattern, startDate, endDate);
//    }
//
//    public List<ChartPointDTO> getSuccessfulTransactionCountChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {
//        return transactionRepository.groupSuccessfulTransactionCountByPeriod(merchantOrgId, pattern, startDate, endDate);
//    }

    public List<ChartPointDTO> getSuccessfulTransactionVolumeChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult = transactionRepository.groupSuccessfulTransactionVolumeByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> result = rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
        return result;
    }


    public List<ChartPointDTO> getSuccessfulTransactionCountChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult =  transactionRepository.groupSuccessfulTransactionCountByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> result = rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
        return result;
    }



}
