package com.bbng.dao.microservices.report.config;

import com.bbng.dao.microservices.report.entity.TransactionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TransactionSpecification {

    public static Specification<TransactionEntity> hasTransactionId(String transactionId) {
        return (root, query, builder) -> builder.equal(root.get("transactionId"), transactionId);
    }

    public static Specification<TransactionEntity> hasMerchantOrgId(String merchantOrgId) {
        return (root, query, builder) -> builder.equal(root.get("merchantOrgId"), merchantOrgId);
    }

    public static Specification<TransactionEntity> hasMerchantName(String merchantName) {
        return (root, query, builder) -> builder.like(root.get("merchantName"), "%" + merchantName + "%");
    }

    public static Specification<TransactionEntity> hasVNuban(String vNuban) {
        return (root, query, builder) -> builder.equal(root.get("vNuban"), vNuban);
    }

    public static Specification<TransactionEntity> isBetweenTimestamps(LocalDateTime start, LocalDateTime end) {
        return (root, query, builder) -> builder.between(root.get("createdAt"), start, end);
    }

    public static Specification<TransactionEntity> hasStatus(String status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }
}
