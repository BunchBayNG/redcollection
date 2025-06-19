package com.bbng.dao.microservices.vacctgen.dto.request;

import lombok.Value;

import java.util.List;


@Value
public class ActivationOperation {
    private String partnerId;
    private String batchRef;
    private List<String> accountNumbers;
    private String accountNumber;

    public enum Operation {
        ACTIVATE, DEACTIVATE
    }
}

