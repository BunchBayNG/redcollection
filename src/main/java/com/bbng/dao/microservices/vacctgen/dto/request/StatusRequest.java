package com.bbng.dao.microservices.vacctgen.dto.request;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class StatusRequest {
    private String accountNo;
}
