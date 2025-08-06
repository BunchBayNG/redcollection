package com.bbng.dao.microservices.report.entity;

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
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String merchantName;
    private String merchantOrgId;
    private String vnuban;
    private BigDecimal amount;
    private String status;
    private String sessionId;
    private String reference;
    private String webhookStatus;
    private String transactionType;
    private String destinationAccountNumber;
    private String destinationAccountName;
    private String destinationBankName;
    private String ipAddress;
//    private String deviceName;
    private Long processingTime;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    // Getters and Setters
}
