//package com.bbng.dao.microservices.vacctnumnotify.controller;
//
//import com.bbng.dao.microservices.auth.organization.service.OrganizationService;
//import com.bbng.dao.util.response.ResponseDto;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/webhook")
//public class WebHookController {
//
//
//
//
//    private final TransactionService transactionService;
//    private final PayoutService payoutService;
//    private final SettlementService settlementService;
//    private final VnubanService vnubanService;
//    private final OrganizationService organizationService;
//    private final CustomerService customerService;
//
//    public WebHookController(TransactionService transactionService, PayoutService payoutService, SettlementService settlementService, VnubanService vnubanService, OrganizationService organizationService, CustomerService customerService) {
//        this.transactionService = transactionService;
//        this.payoutService = payoutService;
//        this.settlementService = settlementService;
//        this.vnubanService = vnubanService;
//        this.organizationService = organizationService;
//        this.customerService = customerService;
//    }
//
//
//    @GetMapping("/va-generate")
//    public   ResponseEntity<ResponseDto<Page<TransactionEntity> >>  getTransactions(@RequestBody TransactionFilterRequestDto request) {
//
//        return  ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactions(request));
//    }
//
//}
