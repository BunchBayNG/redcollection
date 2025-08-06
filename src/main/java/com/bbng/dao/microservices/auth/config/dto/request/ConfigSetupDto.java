package com.bbng.dao.microservices.auth.config.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfigSetupDto {


    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal commissionPercent;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal commissionCap;

    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal adminSplitPercent;

    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal platformSplitPercent;

    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal vatPercent;

    @NotNull
    private String adminAccountNo;

    @NotNull
    private String platformAccountNo;

    @NotNull
    private String vatAccountNo;

    @NotNull
    private String configProductPhone;

    @NotNull
    private String configSupportPhone;

    @NotNull
    private String configSenderEmail;

    @NotNull
    private String configSupportEmail;



    @AssertTrue(message = "adminSplitPercent + platformSplitPercent must be 100")
    public boolean isSplitValid() {
        if (adminSplitPercent == null || platformSplitPercent == null) return true;
        return adminSplitPercent.add(platformSplitPercent).compareTo(BigDecimal.valueOf(100)) == 0;
    }



    private String userId;
    private boolean is2fa;
    private boolean isEmailSetup;
    private boolean isSmsSetup;

}
