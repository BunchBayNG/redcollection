package com.bbng.dao.microservices.vacctgen.dto.response;

import lombok.Value;

@Value
public class ProvisionResult {
    private String accountNo;
    private String accountName;
    private String accountEmail;
    private String initiatorRef;
}
