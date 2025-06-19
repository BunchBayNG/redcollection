package com.bbng.dao.microservices.vacctgen.value;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class MerchantProvisionValue {
    private String accountName;
    private String initiatorRef;
    private String walletNo;
    private BigDecimal amount;
}
