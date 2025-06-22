package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.dto.TopMerchantDTO;
import com.bbng.dao.microservices.report.service.SettlementService;
import com.bbng.dao.microservices.report.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/analytics/transactions")
@RequiredArgsConstructor
public class TransactionAnalyticsController {

    private final TransactionService transactionService;


    @GetMapping("/total-merchants")
    public long getTotalMerchantCount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return transactionService.getTotalMerchantCount(startDate, endDate);
    }

    @GetMapping("/top-merchants")
    public List<TopMerchantDTO> getTopMerchantsByVolume(
            @RequestParam(defaultValue = "5") int topN,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return transactionService.getTopMerchantsByVolume(startDate, endDate, topN);
    }

    @GetMapping("/count-summary")
    public AnalyticsCountSummaryDTO getTransactionCountSummary(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return transactionService.getTransactionCountSummary(merchantOrgId, startDate, endDate);
    }

    @GetMapping("/successful-volume")
    public BigDecimal getSuccessfulTransactionVolume(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return transactionService.getSuccessfulTransactionVolume(merchantOrgId, startDate, endDate);
    }

//    @GetMapping("/successful-rate")
//    public double getSuccessfulTransactionRate(
//            @RequestParam(required = false) Long merchantOrgId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
//    ) {
//        return transactionService.getSuccessfulTransactionRate(merchantOrgId, startDate, endDate);
//    }

    @GetMapping("/successful-volume-chart")
    public List<ChartPointDTO> getSuccessfulTransactionVolumeChart(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);
        return transactionService.getSuccessfulTransactionVolumeChart(merchantOrgId, pattern, startDate, endDate);
    }

    @GetMapping("/successful-count-chart")
    public List<ChartPointDTO> getSuccessfulTransactionCountChart(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);
        return transactionService.getSuccessfulTransactionCountChart(merchantOrgId, pattern, startDate, endDate);
    }

    private String resolvePattern(String period) {
        return switch (period.toLowerCase()) {
            case "daily" -> "%Y-%m-%d";
            case "monthly" -> "%Y-%m";
            case "yearly" -> "%Y";
            default -> throw new IllegalArgumentException("Invalid period: " + period);
        };
    }
}

