package com.bbng.dao.microservices.vacctgen.dto.request;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MerchantStatusRequest extends StatusRequest {
    private String initiatorRef;

    public MerchantStatusRequest(String accountNo, String initiatorRef) {
        super(accountNo);
        this.initiatorRef = initiatorRef;
    }
}
