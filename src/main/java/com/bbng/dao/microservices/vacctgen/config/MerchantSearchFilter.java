package com.bbng.dao.microservices.vacctgen.config;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MerchantSearchFilter extends SearchFilter {
    private String walletNo;

    public MerchantSearchFilter(String accountNo,
                                String initiatorRef,
                                BigDecimal amount,
                                String accountName,
                                LocalDateTime fromDate,
                                LocalDateTime toDate,
                                String walletNo) {
        super(accountNo, initiatorRef, amount, accountName, fromDate, toDate);
        this.walletNo = walletNo;
    }
}
