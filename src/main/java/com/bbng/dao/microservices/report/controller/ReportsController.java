package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.service.OrganizationService;
import com.bbng.dao.microservices.report.dto.*;
import com.bbng.dao.microservices.report.entity.*;
import com.bbng.dao.microservices.report.service.*;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportsController {

    private final TransactionService transactionService;
    private final PayoutService payoutService;
    private final SettlementService settlementService;
    private final VnubanService vnubanService;
    private final OrganizationService organizationService;
    private final CustomerService customerService;

    public ReportsController(TransactionService transactionService, PayoutService payoutService, SettlementService settlementService, VnubanService vnubanService, OrganizationService organizationService, CustomerService customerService) {
        this.transactionService = transactionService;
        this.payoutService = payoutService;
        this.settlementService = settlementService;
        this.vnubanService = vnubanService;
        this.organizationService = organizationService;
        this.customerService = customerService;
    }








    private final String defaultPage = "0";      // default page
    private final String defaultSize = "10";     // default size
    private final String defaultSortOrder = "ASC";     // default size


    @GetMapping("/transactions")
    public   ResponseEntity<ResponseDto<Page<TransactionEntity> >>  getTransactions(@RequestParam TransactionFilterRequestDto request) {

        return  ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactions(request));
    }


    @GetMapping("/payouts")
    public   ResponseEntity<ResponseDto<Page<PayoutEntity> >>  getPayouts(@RequestParam PayoutFilterRequestDto request) {

        return  ResponseEntity.status(HttpStatus.OK).body(payoutService.getPayouts(request));
    }

    @GetMapping("/settlements")
    public   ResponseEntity<ResponseDto<Page<SettlementEntity> >>  getSettlements(@RequestParam SettlementFilterRequestDto request) {

        return  ResponseEntity.status(HttpStatus.OK).body(settlementService.getSettlements(request));
    }


    @GetMapping("/vnubans")
    public   ResponseEntity<ResponseDto<Page<VnubanEntity> >>  getVnubans(@RequestParam VnubanFilterRequestDto request ) {

        return  ResponseEntity.status(HttpStatus.OK).body(vnubanService.getVnubans(request));
    }

    @GetMapping("/organizations")
    public   ResponseEntity<ResponseDto<Page<OrganizationEntity> >>  getOrg(

            @RequestParam(required = false) String search,
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = defaultSortOrder) String sortOrder,
            @RequestParam(defaultValue = defaultPage) int page,
            @RequestParam(defaultValue = defaultSize) int size

    ) {

        return  ResponseEntity.status(HttpStatus.OK).body(
                organizationService.getAllOrg(search, merchantOrgId,status, sortBy, sortOrder, startDate, endDate, page, size  ));
    }

    @GetMapping("/organization-customers")
    public   ResponseEntity<ResponseDto<Page<CustomerEntity> >>  getOrgCustomers(@RequestParam CustomerFilterRequestDto request ) {

        return  ResponseEntity.status(HttpStatus.OK).body(customerService.getOrgCustomers(request));
    }
}
