package com.bbng.dao.microservices.report.controller;


import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.service.PayoutService;
import com.bbng.dao.microservices.report.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



@RestController
@RequestMapping("/api/analytics/payouts")
@RequiredArgsConstructor
public class PayoutAnalyticsController {

    private final PayoutService payoutService;

    @GetMapping("/count-summary")
    public AnalyticsCountSummaryDTO getPayoutCountSummary(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return payoutService.getPayoutCountSummary(merchantOrgId, startDate, endDate);
    }

    @GetMapping("/successful-volume")
    public BigDecimal getSuccessfulPayoutVolume(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return payoutService.getSuccessfulPayoutVolume(merchantOrgId, startDate, endDate);
    }

//    @GetMapping("/successful-rate")
//    public double getSuccessfulPayoutRate(
//            @RequestParam(required = false) Long merchantOrgId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
//    ) {
//        return payoutService.getSuccessfulPayoutRate(merchantOrgId, startDate, endDate);
//    }

    @GetMapping("/successful-volume-chart")
    public List<ChartPointDTO> getSuccessfulPayoutVolumeChart(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);
        return payoutService.getSuccessfulPayoutVolumeChart(merchantOrgId, pattern, startDate, endDate);
    }

    @GetMapping("/successful-count-chart")
    public List<ChartPointDTO> getSuccessfulPayoutCountChart(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);
        return payoutService.getSuccessfulPayoutCountChart(merchantOrgId, pattern, startDate, endDate);
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
