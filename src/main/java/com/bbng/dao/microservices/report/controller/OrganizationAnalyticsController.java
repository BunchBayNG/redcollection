package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.auth.organization.service.OrganizationService;
import com.bbng.dao.util.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("${apiVersion}" +  "/analytics/merchants")
@RequiredArgsConstructor
public class OrganizationAnalyticsController {


    private final OrganizationService organizationService;

    @GetMapping("/total")
    public ResponseEntity<ResponseDto<Long>> getTotalMerchantCount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {

        return  ResponseEntity.status(HttpStatus.OK).body(organizationService.getNewMerchantCount(startDate, endDate));
    }

//
//    @GetMapping("/all")
//    public ResponseEntity<ResponseDto<Long>> getNewMerchantCount() {
//        return  ResponseEntity.status(HttpStatus.OK).body(organizationService.getTotalMerchantCount());
//    }

}
