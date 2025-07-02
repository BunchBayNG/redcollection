package com.bbng.dao.microservices.vacctgen.config;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Value
@NonFinal
public class SearchFilter {
    private String accountNo;
    private String initiatorRef;
    private BigDecimal amount;
    private String accountName;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;
}
