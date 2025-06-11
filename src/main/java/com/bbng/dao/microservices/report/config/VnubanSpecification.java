package com.bbng.dao.microservices.report.config;

import com.bbng.dao.microservices.report.entity.VnubanEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class VnubanSpecification {

    public static Specification<VnubanEntity> hasVnubanType(String vnubanType) {
        return (root, query, builder) -> builder.equal(root.get("vnubanType"), vnubanType);
    }
    public static Specification<VnubanEntity> hasMerchantOrgId(String merchantOrgId) {
        return (root, query, builder) -> builder.equal(root.get("merchantOrgId"), merchantOrgId);
    }

    public static Specification<VnubanEntity> hasMerchantName(String merchantName) {
        return (root, query, builder) -> builder.like(root.get("merchantName"), "%" + merchantName + "%");
    }

    public static Specification<VnubanEntity> hasVNuban(String vNuban) {
        return (root, query, builder) -> builder.equal(root.get("vNuban"), vNuban);
    }

    public static Specification<VnubanEntity> isBetweenTimestamps(LocalDateTime start, LocalDateTime end) {
        return (root, query, builder) -> builder.between(root.get("timestamp"), start, end);
    }

    public static Specification<VnubanEntity> hasStatus(String status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }
}
