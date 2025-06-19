package com.bbng.dao.microservices.vacctgen.dto.request;

import lombok.Value;

@Value
public class ProvisionRequest {
    private String accountName;
    private String accountMsisdn;
    private String accountEmail;
    private String initiatorRef;
}
