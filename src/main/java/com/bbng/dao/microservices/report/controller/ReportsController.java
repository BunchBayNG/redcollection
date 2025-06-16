package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.report.dto.PayoutFilterRequestDto;
import com.bbng.dao.microservices.report.dto.SettlementFilterRequestDto;
import com.bbng.dao.microservices.report.dto.TransactionFilterRequestDto;
import com.bbng.dao.microservices.report.dto.VnubanFilterRequestDto;
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
}
