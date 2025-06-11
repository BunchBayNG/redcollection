package com.bbng.dao.microservices.report.config;

import com.bbng.dao.microservices.report.entity.SettlementEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class SettlementSpecification {

    public static Specification<SettlementEntity> hasSourceAccount(String sourceAccount) {
        return (root, query, builder) -> builder.equal(root.get("sourceAccount"), sourceAccount);


    }
    public static Specification<SettlementEntity> hasMerchantOrgId(String merchantOrgId) {
        return (root, query, builder) -> builder.equal(root.get("merchantOrgId"), merchantOrgId);
    }

    public static Specification<SettlementEntity> hasDestinationAccount(String destinationAccount) {
        return (root, query, builder) -> builder.equal(root.get("destinationAccount"), destinationAccount);
    }

    public static Specification<SettlementEntity> hasMerchantName(String merchantName) {
        return (root, query, builder) -> builder.like(root.get("merchantName"), "%" + merchantName + "%");
    }

    public static Specification<SettlementEntity> hasTransactionRef(String transactionRef) {
        return (root, query, builder) -> builder.equal(root.get("transactionRef"), transactionRef);
    }

    public static Specification<SettlementEntity> hasReference(String reference) {
        return (root, query, builder) -> builder.equal(root.get("reference"), reference);
    }

    public static Specification<SettlementEntity> isBetweenTimestamps(LocalDateTime start, LocalDateTime end) {
        return (root, query, builder) -> builder.between(root.get("createdAt"), start, end);
    }

    public static Specification<SettlementEntity> hasStatus(String status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }
}
