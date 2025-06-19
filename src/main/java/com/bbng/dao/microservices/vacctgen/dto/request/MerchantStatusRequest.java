package com.bbng.dao.microservices.vacctgen.dto.request;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MerchantStatusRequest extends StatusRequest {
    private String initiatorRef;
    private String walletNo;

    public MerchantStatusRequest(String accountNo, String initiatorRef, String walletNo) {
        super(accountNo);
        this.initiatorRef = initiatorRef;
        this.walletNo = walletNo;
    }
}
