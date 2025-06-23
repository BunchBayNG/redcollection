package com.bbng.dao.microservices.vacctgen.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
                @Index(name = "idx_client_Id", columnList = "clientId")
        }
)
public class ProvisionedAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNo;

    private String clientId;
    private String clientName;
    private String accountName;
    private String accountMsisdn;
    private String accountEmail;
    private String initiatorRef;

    private BigDecimal amount;
    private String walletNo;
    private String batchRef;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss a")
    private Date provisionDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Mode mode;


    public enum Status {
        CANCELLED, INACTIVE, ACTIVE
    }

    public enum Mode {
        OPEN, CLOSED
    }
}
