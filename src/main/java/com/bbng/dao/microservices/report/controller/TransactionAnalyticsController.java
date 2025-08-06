package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.dto.TopMerchantDTO;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.microservices.report.service.TransactionService;
import com.bbng.dao.util.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("${apiVersion}" + "/analytics/transactions")
@RequiredArgsConstructor
public class TransactionAnalyticsController {

    private final TransactionService transactionService;


    @GetMapping("/top-merchants")
    public ResponseEntity<ResponseDto<List<TopMerchantDTO>>>  getTopMerchantsByVolume(
            @RequestParam(defaultValue = "5") int topN,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {

        return  ResponseEntity.status(HttpStatus.OK).body(transactionService
                .getTopMerchantsByVolume(startDate, endDate, topN));

    }

    @GetMapping("/count-summary")
    public ResponseEntity<ResponseDto<AnalyticsCountSummaryDTO>>  getTransactionCountSummary(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {

        return  ResponseEntity.status(HttpStatus.OK).body(transactionService
                .getTransactionCountSummary(merchantOrgId, startDate, endDate));
    }

    @GetMapping("/successful-volume")
    public ResponseEntity<ResponseDto<BigDecimal>> getSuccessfulTransactionVolume(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return  ResponseEntity.status(HttpStatus.OK).body(transactionService
                .getSuccessfulTransactionVolume(merchantOrgId, startDate, endDate));

    }


    @GetMapping("/successful-volume-chart")
    public ResponseEntity<ResponseDto<List<ChartPointDTO>>>  getSuccessfulTransactionVolumeChart(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);

        return  ResponseEntity.status(HttpStatus.OK).body(transactionService
                .getSuccessfulTransactionVolumeChart(merchantOrgId,pattern, startDate, endDate));
    }

    @GetMapping("/successful-count-chart")
    public ResponseEntity<ResponseDto<List<ChartPointDTO>>> getSuccessfulTransactionCountChart(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);

        return  ResponseEntity.status(HttpStatus.OK).body(transactionService
                .getSuccessfulTransactionCountChart(merchantOrgId, pattern,  startDate, endDate));
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

