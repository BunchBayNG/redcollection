package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.service.SettlementService;
import com.bbng.dao.util.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("${apiVersion}" +  "/analytics/settlements")
@RequiredArgsConstructor
public class SettlementAnalyticsController {

    private final SettlementService settlementService;

    @GetMapping("/count-summary")
    public ResponseEntity<ResponseDto<AnalyticsCountSummaryDTO>>  getSettlementCountSummary(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return  ResponseEntity.status(HttpStatus.OK).body(settlementService
                .getSettlementCountSummary(merchantOrgId, startDate, endDate));
    }

    @GetMapping("/successful-volume")
    public ResponseEntity<ResponseDto<BigDecimal>>   getSuccessfulSettlementVolume(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return  ResponseEntity.status(HttpStatus.OK).body(settlementService.
                getSuccessfulSettlementVolume(merchantOrgId, startDate, endDate));
    }


    @GetMapping("/successful-volume-chart")
    public ResponseEntity<ResponseDto<List<ChartPointDTO> >> getSuccessfulSettlementVolumeChart(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);
        return  ResponseEntity.status(HttpStatus.OK).body(settlementService
                .getSuccessfulSettlementVolumeChart(merchantOrgId, pattern, startDate, endDate));
    }

    @GetMapping("/successful-count-chart")
    public ResponseEntity<ResponseDto<List<ChartPointDTO>>>  getSuccessfulSettlementCountChart(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);

        return  ResponseEntity.status(HttpStatus.OK).body(settlementService
                .getSuccessfulSettlementCountChart(merchantOrgId, pattern, startDate, endDate));
    }

   private String resolvePattern(String period) {
    return switch (period.toLowerCase()) {
        case "daily" -> "%Y-%m-%d";
        case "weekly" -> "%Y-%u"; // year + week number
        case "monthly" -> "%Y-%m";
        case "yearly" -> "%Y";
        default -> throw new IllegalArgumentException("Invalid period: " + period);
    };
}
}
