package com.bbng.dao.microservices.auth.config.dto.request;

import com.bbng.dao.microservices.auth.config.enums.FeeType;
import com.bbng.dao.microservices.auth.config.enums.ServiceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceConfigRequest {

    @NotNull(message = "Service type is required")
    private ServiceType serviceType;

    @NotNull(message = "Fee is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Fee must be 0 or greater")
    private BigDecimal fee;

    @NotNull(message = "Fee type is required")
    private FeeType feeType;

    // Cap is optional but conditionally validated in custom validator
    private BigDecimal cap;

    // Getters and Setters
}