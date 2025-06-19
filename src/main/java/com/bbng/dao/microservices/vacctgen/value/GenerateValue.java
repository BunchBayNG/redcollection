package com.bbng.dao.microservices.vacctgen.value;

import lombok.Value;

@Value
public class GenerateValue {
    private final String prefix;
    private final Integer size;
}
