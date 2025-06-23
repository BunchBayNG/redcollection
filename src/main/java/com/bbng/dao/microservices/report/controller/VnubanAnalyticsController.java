package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.service.VnubanService;
import com.bbng.dao.util.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDto<Long>>  getTotalVnubans(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return  ResponseEntity.status(HttpStatus.OK).body(vnubanService
                .getTotalVnubans(merchantOrgId, startDate, endDate));
    }

    @GetMapping("/total-static")
    public ResponseEntity<ResponseDto<Long>>  getTotalStaticVnubans(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {

        return  ResponseEntity.status(HttpStatus.OK).body(vnubanService
                .getTotalStaticVnubans(merchantOrgId, startDate, endDate));
    }

    @GetMapping("/total-dynamic")
    public ResponseEntity<ResponseDto<Long>>  getTotalDynamicVnubans(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {

        return  ResponseEntity.status(HttpStatus.OK).body(vnubanService
                .getTotalDynamicVnubans(merchantOrgId, startDate, endDate));


    }

    @GetMapping("/generated-chart")
    public ResponseEntity<ResponseDto<List<ChartPointDTO>>>  getGeneratedVnubansChart(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        String pattern = resolvePattern(period);

       return  ResponseEntity.status(HttpStatus.OK).body(vnubanService
                .getGeneratedVnubansChart(merchantOrgId, pattern,  startDate, endDate));
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
