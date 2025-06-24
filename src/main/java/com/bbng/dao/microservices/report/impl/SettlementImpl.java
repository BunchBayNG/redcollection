package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.SettlementSpecification;
import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.entity.SettlementEntity;
import com.bbng.dao.microservices.report.repository.SettlementRepository;
import com.bbng.dao.microservices.report.service.SettlementService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

//    public static String generateRandomDigits() {
//        int randomDigits = 1000 + new SecureRandom().nextInt(9000);
//        return "STRX-" + randomDigits;
//    }



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
