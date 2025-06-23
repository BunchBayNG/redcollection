package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.service.VnubanService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;



@RestController
@RequestMapping("${apiVersion}" + "/analytics/vnubans")
@RequiredArgsConstructor
public class VnubanAnalyticsController {

    private final VnubanService vnubanService;

    @GetMapping("/total")
    public long getTotalVnubans(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return vnubanService.getTotalVnubans(merchantOrgId, startDate, endDate);
    }

    @GetMapping("/total-static")
    public long getTotalStaticVnubans(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return vnubanService.getTotalStaticVnubans(merchantOrgId, startDate, endDate);
    }

    @GetMapping("/total-dynamic")
    public long getTotalDynamicVnubans(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return vnubanService.getTotalDynamicVnubans(merchantOrgId, startDate, endDate);
    }

    @GetMapping("/generated-chart")
    public List<ChartPointDTO> getGeneratedVnubansChart(
            @RequestParam(required = false) Long merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);
        return vnubanService.getGeneratedVnubansChart(merchantOrgId, pattern, startDate, endDate);
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
