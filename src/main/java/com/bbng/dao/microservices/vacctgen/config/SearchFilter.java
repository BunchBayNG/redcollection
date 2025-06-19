package com.bbng.dao.microservices.vacctgen.config;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Value
@NonFinal
public class SearchFilter {
    private String accountNo;
    private String initiatorRef;
    private BigDecimal amount;
    private String accountName;

    @DateTimeFormat(
            pattern = "dd/MM/yyyy HH:mm:ss",
            fallbackPatterns = {"dd/MM/yyyy HH:mm", "dd/MM/yyyy"}
    )
    private Date fromDate;

    @DateTimeFormat(
            pattern = "dd/MM/yyyy HH:mm:ss",
            fallbackPatterns = {"dd/MM/yyyy HH:mm", "dd/MM/yyyy"}
    )
    private Date toDate;
}
