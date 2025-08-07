package com.bbng.dao.microservices.report.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDTO {
//
//    @NotNull
//    private String merchantName;
//
//    @NotNull
//    private String merchantOrgId;

    @NotNull
    private String vnuban;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String status;

    @NotNull
    private String sessionId;

    @NotNull
    private String reference;

    @NotNull
    private String transactionType;

    @NotNull
    private String destinationAccountNumber;

    @NotNull
    private String destinationAccountName;

    @NotNull
    private String destinationBankName;

}
