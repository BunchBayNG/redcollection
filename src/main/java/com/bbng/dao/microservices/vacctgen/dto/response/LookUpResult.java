package com.bbng.dao.microservices.vacctgen.dto.response;

import lombok.Value;

@Value
public class LookUpResult {
    private String accountNo;
    private String accountName;
    private String merchantOrgId;
    private String walletNo;
}



