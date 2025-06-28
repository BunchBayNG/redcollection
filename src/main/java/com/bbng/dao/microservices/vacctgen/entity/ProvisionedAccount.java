package com.bbng.dao.microservices.vacctgen.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Access(AccessType.FIELD)
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(
        indexes = {
                @Index(name = "idx_wallet_no", columnList = "walletNo"),
                @Index(name = "idx_account_no", columnList = "accountNo"),
                @Index(name = "idx_initiator_ref", columnList = "initiatorRef"),
                @Index(name = "idx_merchant_org_Id", columnList = "merchantOrgId")
        }
)
public class ProvisionedAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNo;
    private String merchantOrgId;
    private String merchantName;
    private String accountName;
    private String accountMsisdn;
    private String accountEmail;
    private String initiatorRef;

    private BigDecimal amount;
    private String walletNo;
    private String batchRef;


    @CreationTimestamp
    private LocalDateTime provisionDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    private String productType;

    public enum Status {
        CANCELLED, INACTIVE, ACTIVE
    }

    public enum Mode {
        OPEN, CLOSED
    }

}
