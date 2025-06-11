package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.report.entity.PayoutEntity;
import com.bbng.dao.microservices.report.entity.SettlementEntity;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.microservices.report.entity.VnubanEntity;
import com.bbng.dao.microservices.report.service.PayoutService;
import com.bbng.dao.microservices.report.service.SettlementService;
import com.bbng.dao.microservices.report.service.TransactionService;
import com.bbng.dao.microservices.report.service.VnubanService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {




    private final TransactionService transactionService;
    private final PayoutService payoutService;
    private final SettlementService settlementService;
    private final VnubanService vnubanService;

    public ReportsController(TransactionService transactionService,
                             PayoutService payoutService,
                             SettlementService settlementService,
                             VnubanService vnubanService) {
        this.transactionService = transactionService;
        this.payoutService = payoutService;
        this.settlementService = settlementService;
        this.vnubanService = vnubanService;
    }


    @GetMapping("/transactions")
    public   ResponseEntity<ResponseDto<Page<TransactionEntity> >>  getTransactions(
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) String vNuban,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return  ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactions(transactionId,
                merchantName,  merchantOrgId,
                vNuban, startDate, endDate, status,
                sortBy, ascending, page, size));
    }


    @GetMapping("/payouts")
    public   ResponseEntity<ResponseDto<Page<PayoutEntity> >>  getPayouts(
            @RequestParam(required = false) String sourceAccount,
            @RequestParam(required = false) String destinationAccount,
            @RequestParam(required = false) String transactionRef,
            @RequestParam(required = false) String paymentReference,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return  ResponseEntity.status(HttpStatus.OK).body(payoutService.getPayouts(sourceAccount, merchantName, merchantOrgId,
                destinationAccount,  transactionRef,  paymentReference, startDate, endDate, status,
                sortBy, ascending, page, size));
    }
 @GetMapping("/settlements")
    public   ResponseEntity<ResponseDto<Page<SettlementEntity> >>  getSettlements(
            @RequestParam(required = false) String sourceAccount,
            @RequestParam(required = false) String destinationAccount,
            @RequestParam(required = false) String transactionRef,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return  ResponseEntity.status(HttpStatus.OK).body(settlementService.getSettlements(sourceAccount,
                merchantName,  merchantOrgId, destinationAccount,  transactionRef,  reference, startDate, endDate, status,
                sortBy, ascending, page, size));
    }


    @GetMapping("/vnubans")
    public   ResponseEntity<ResponseDto<Page<VnubanEntity> >>  getVnubans(
            @RequestParam(required = false) String vnubanType,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) String vNuban,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return  ResponseEntity.status(HttpStatus.OK).body(vnubanService.getVnubans(vnubanType, merchantName, merchantOrgId,
                vNuban, startDate, endDate, status,
                sortBy, ascending, page, size));
    }
}
