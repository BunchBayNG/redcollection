package com.bbng.dao.microservices.vacctgen.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;


@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ActivationResponse {
    private Integer numberActivated;
    private Integer numberDeactivated;
}
