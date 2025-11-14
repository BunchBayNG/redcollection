package com.bbng.dao.microservices.vacctnumnotify.controller;

import com.bbng.dao.microservices.report.dto.TransactionRequestDTO;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.microservices.report.service.TransactionService;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiVersion}" +  "/webhook/receive")
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
