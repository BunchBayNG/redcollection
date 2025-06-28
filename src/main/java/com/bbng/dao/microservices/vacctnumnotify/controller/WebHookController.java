package com.bbng.dao.microservices.vacctnumnotify.controller;

import com.bbng.dao.microservices.report.dto.TransactionRequestDTO;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.microservices.report.service.*;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook/receive")
public class WebHookController {


    private final TransactionService transactionService;


    public WebHookController(TransactionService transactionService) {
        this.transactionService = transactionService;

    }


    @PostMapping("/va-transaction")
    public   ResponseEntity<ResponseDto<TransactionEntity>>  getTransactions(@RequestBody @Valid TransactionRequestDTO request) {

        return  ResponseEntity.status(HttpStatus.OK).body(transactionService.createTransaction(request));
    }

}
