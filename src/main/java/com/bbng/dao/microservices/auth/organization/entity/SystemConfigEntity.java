package com.bbng.dao.microservices.auth.organization.entity;


import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class SystemConfigEntity extends BaseEntity {
    @Id
    private Long id = 1L; // singleton row

    private BigDecimal commissionPercent;   // e.g., 5.0

    private BigDecimal commissionCap;       // e.g., 500.0

    private BigDecimal adminSplitPercent;   // e.g., 70.0

    private BigDecimal platformSplitPercent; // e.g., 30.0

    private BigDecimal vatPercent;          // e.g., 7.5

    private Long adminAccountNo;

    private Long platformAccountNo;

    private Long vatAccountNo;


    private String configProductPhone;


    private String configSupportPhone;


    private String configSenderEmail;


    private String configSupportEmail;


/*    private boolean is2FA;

    private boolean isEmailSetUp;

    private boolean isSmsSetUp;

    @Column(unique = true)
    private String userId;
    */

}
