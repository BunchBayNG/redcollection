package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.service.OrganizationService;
import com.bbng.dao.microservices.report.entity.PayoutEntity;
import com.bbng.dao.microservices.report.entity.SettlementEntity;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.microservices.report.service.*;
import com.bbng.dao.microservices.vacctgen.entity.ProvisionedAccount;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("${apiVersion}" + "/reports")
public class ReportsController {

    private final TransactionService transactionService;
    private final PayoutService payoutService;
    private final SettlementService settlementService;
    private final VnubanService vnubanService;
    private final OrganizationService organizationService;
    private final CustomerService customerService;

    public ReportsController(TransactionService transactionService, PayoutService payoutService,
                             SettlementService settlementService, VnubanService vnubanService,
                             OrganizationService organizationService, CustomerService customerService) {
        this.transactionService = transactionService;
        this.payoutService = payoutService;
        this.settlementService = settlementService;
        this.vnubanService = vnubanService;
        this.organizationService = organizationService;
        this.customerService = customerService;
    }

    private final String defaultPage = "0";      // default page
    private final String defaultSize = "10";     // default size
    private final String defaultSortOrder = "DESC";     // default sort order, Ascending or Descending


    @GetMapping("/transactions")
    public   ResponseEntity<ResponseDto<Page<TransactionEntity> >>  getTransactions(
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

        return  ResponseEntity.status(HttpStatus.OK).body(transactionService
                .getTransactions(search, merchantOrgId,status, sortBy, sortOrder, startDate, endDate, page, size ));
    }


    @GetMapping("/payouts")
    public   ResponseEntity<ResponseDto<Page<PayoutEntity> >>  getPayouts(
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

        return  ResponseEntity.status(HttpStatus.OK).body(payoutService
                .getPayouts(search, merchantOrgId,status, sortBy, sortOrder, startDate, endDate, page, size ));
    }

    @GetMapping("/settlements")
    public   ResponseEntity<ResponseDto<Page<SettlementEntity> >>  getSettlements(
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

        return  ResponseEntity.status(HttpStatus.OK).body(settlementService
                .getSettlements(search, merchantOrgId,status, sortBy, sortOrder, startDate, endDate, page, size ));
    }


    @GetMapping("/vnubans")
    public   ResponseEntity<ResponseDto<Page<ProvisionedAccount> >>  getVnubans(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "provisionDate") String sortBy,
            @RequestParam(defaultValue = defaultSortOrder) String sortOrder,
            @RequestParam(defaultValue = defaultPage) int page,
            @RequestParam(defaultValue = defaultSize) int size

    ) {

        return  ResponseEntity.status(HttpStatus.OK).body(vnubanService
                .getVnubans(search, merchantOrgId,status, sortBy, sortOrder, startDate, endDate, page, size ));
    }

    @GetMapping("/organizations")
    public   ResponseEntity<ResponseDto<Page<OrganizationEntity> >>  getOrg(

            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = defaultSortOrder) String sortOrder,
            @RequestParam(defaultValue = defaultPage) int page,
            @RequestParam(defaultValue = defaultSize) int size

    ) {

        return  ResponseEntity.status(HttpStatus.OK).body(organizationService
                .getAllOrg(search,status, sortBy, sortOrder, startDate, endDate, page, size  ));
    }

//    @GetMapping("/organization-customers")
//    public   ResponseEntity<ResponseDto<Page<CustomerEntity> >>  getOrgCustomers(
//            @RequestParam(required = false) String search,
//            @RequestParam(required = false) String merchantOrgId,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
//            @RequestParam(required = false) String status,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = defaultSortOrder) String sortOrder,
//            @RequestParam(defaultValue = defaultPage) int page,
//            @RequestParam(defaultValue = defaultSize) int size
//    ) {
//
//        return  ResponseEntity.status(HttpStatus.OK).body(customerService
//                .getOrgCustomers(search, merchantOrgId,status, sortBy, sortOrder, startDate, endDate, page, size ));
//    }


}
