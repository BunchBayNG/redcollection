package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.PayoutSpecification;
import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.entity.PayoutEntity;
import com.bbng.dao.microservices.report.repository.PayoutRepository;
import com.bbng.dao.microservices.report.service.PayoutService;
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



    public AnalyticsCountSummaryDTO getPayoutCountSummary(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        long total = payoutRepository.countByCreatedAtBetween(merchantOrgId, startDate, endDate);
        long success = payoutRepository.countByStatusAndCreatedAtBetween(merchantOrgId,"SUCCESS", startDate, endDate);
        long pending = payoutRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "PENDING", startDate, endDate);
        long failed = payoutRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "FAILED", startDate, endDate);

        return new AnalyticsCountSummaryDTO(total, success, pending, failed);
    }

    public BigDecimal getSuccessfulPayoutVolume(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        return payoutRepository.sumAmountByStatus(merchantOrgId,"SUCCESS", startDate, endDate);
    }

    public double getSuccessfulPayoutRate(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        long total = payoutRepository.countByCreatedAtBetween(merchantOrgId, startDate, endDate);
        long success = payoutRepository.countByStatusAndCreatedAtBetween(merchantOrgId, "SUCCESS", startDate, endDate);

        return total == 0 ? 0 : (double) success / total * 100;
    }

    public List<ChartPointDTO> getSuccessfulPayoutVolumeChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult = payoutRepository.groupSuccessfulPayoutVolumeByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> result = rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
        return result;
    }


    public List<ChartPointDTO> getSuccessfulPayoutCountChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult =  payoutRepository.groupSuccessfulPayoutCountByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> result = rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
        return result;
    }







}


