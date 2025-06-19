package com.bbng.dao.microservices.vacctgen.dto.response;

import lombok.Value;

@Value
public class ConfirmLookupResult {
    private String accountNo;
    private String accountName;
    private String walletNo;
    private boolean success;
}
