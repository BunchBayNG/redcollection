package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/analytics/settlements")
@RequiredArgsConstructor
public class SettlementAnalyticsController {

    private final SettlementService settlementService;

    @GetMapping("/count-summary")
    public AnalyticsCountSummaryDTO getSettlementCountSummary(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return settlementService.getSettlementCountSummary(merchantOrgId, startDate, endDate);
    }

    @GetMapping("/successful-volume")
    public BigDecimal getSuccessfulSettlementVolume(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return settlementService.getSuccessfulSettlementVolume(merchantOrgId, startDate, endDate);
    }

//    @GetMapping("/successful-rate")
//    public double getSuccessfulSettlementRate(
//            @RequestParam(required = false) Long merchantOrgId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
//    ) {
//        return settlementService.getSuccessfulSettlementRate(merchantOrgId, startDate, endDate);
//    }

    @GetMapping("/successful-volume-chart")
    public List<ChartPointDTO> getSuccessfulSettlementVolumeChart(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);
        return settlementService.getSuccessfulSettlementVolumeChart(merchantOrgId, pattern, startDate, endDate);
    }

    @GetMapping("/successful-count-chart")
    public List<ChartPointDTO> getSuccessfulSettlementCountChart(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);
        return settlementService.getSuccessfulSettlementCountChart(merchantOrgId, pattern, startDate, endDate);
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
