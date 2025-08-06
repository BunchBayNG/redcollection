package com.bbng.dao.microservices.report.entity;

import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SettlementEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private String merchantName;
    private String merchantOrgId;
    private String sourceAccount;
    private String destinationAccount;
    private String status;
    private String transactionRef;
    private String reference;
    private String settlementRef;

    // Getters and Setters
}
