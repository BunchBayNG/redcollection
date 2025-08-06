package com.bbng.dao.microservices.vacctgen.value;

import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

@Data
public class MerchantProvisionValue {
    private String accountName;
    private String initiatorRef;
    private String productType;
    private BigDecimal amount;
}
