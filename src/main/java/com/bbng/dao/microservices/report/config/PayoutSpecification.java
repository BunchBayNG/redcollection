package com.bbng.dao.microservices.report.config;

import com.bbng.dao.microservices.report.entity.PayoutEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PayoutSpecification {

    public static Specification<PayoutEntity> hasSourceAccount(String sourceAccount) {
        return (root, query, builder) -> builder.equal(root.get("sourceAccount"), sourceAccount);

    }

    public static Specification<PayoutEntity> hasMerchantOrgId(String merchantOrgId) {
        return (root, query, builder) -> builder.equal(root.get("merchantOrgId"), merchantOrgId);
    }

    public static Specification<PayoutEntity> hasDestinationAccount(String destinationAccount) {
        return (root, query, builder) -> builder.equal(root.get("destinationAccount"), destinationAccount);
    }

    public static Specification<PayoutEntity> hasMerchantName(String merchantName) {
        return (root, query, builder) -> builder.like(root.get("merchantName"), "%" + merchantName + "%");
    }

    public static Specification<PayoutEntity> hasTransactionRef(String transactionRef) {
        return (root, query, builder) -> builder.equal(root.get("transactionRef"), transactionRef);
    }

    public static Specification<PayoutEntity> hasPaymentReference(String paymentReference) {
        return (root, query, builder) -> builder.equal(root.get("paymentReference"), paymentReference);
    }

    public static Specification<PayoutEntity> isBetweenTimestamps(LocalDateTime start, LocalDateTime end) {
        return (root, query, builder) -> builder.between(root.get("createdAt"), start, end);
    }

    public static Specification<PayoutEntity> hasStatus(String status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }
}
