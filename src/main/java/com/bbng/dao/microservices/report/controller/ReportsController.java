package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.service.OrganizationService;
import com.bbng.dao.microservices.report.dto.*;
import com.bbng.dao.microservices.report.entity.*;
import com.bbng.dao.microservices.report.service.*;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
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


    @GetMapping("/transactions")
    public   ResponseEntity<ResponseDto<Page<TransactionEntity> >>  getTransactions(@RequestBody TransactionFilterRequestDto request) {

        return  ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactions(request));
    }


    @GetMapping("/payouts")
    public   ResponseEntity<ResponseDto<Page<PayoutEntity> >>  getPayouts(@RequestBody PayoutFilterRequestDto request) {

        return  ResponseEntity.status(HttpStatus.OK).body(payoutService.getPayouts(request));
    }

    @GetMapping("/settlements")
    public   ResponseEntity<ResponseDto<Page<SettlementEntity> >>  getSettlements(@RequestBody SettlementFilterRequestDto request) {

        return  ResponseEntity.status(HttpStatus.OK).body(settlementService.getSettlements(request));
    }


    @GetMapping("/vnubans")
    public   ResponseEntity<ResponseDto<Page<VnubanEntity> >>  getVnubans(@RequestBody VnubanFilterRequestDto request ) {

        return  ResponseEntity.status(HttpStatus.OK).body(vnubanService.getVnubans(request));
    }

    @GetMapping("/organizations")
    public   ResponseEntity<ResponseDto<Page<OrganizationEntity> >>  getOrg(@RequestBody OrgFilterRequestDto request ) {

        return  ResponseEntity.status(HttpStatus.OK).body(organizationService.getAllOrg(request));
    }

    @GetMapping("/organization-customers")
    public   ResponseEntity<ResponseDto<Page<CustomerEntity> >>  getOrgCustomers(@RequestBody CustomerFilterRequestDto request ) {

        return  ResponseEntity.status(HttpStatus.OK).body(customerService.getOrgCustomers(request));
    }
}
