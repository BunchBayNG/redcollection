package com.bbng.dao.microservices.auth.auditlog.impl;

import com.bbng.dao.microservices.auth.auditlog.dto.request.AuditLogFilterRequest;
import com.bbng.dao.microservices.auth.auditlog.entities.AuditLogEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLogSpecification {

    public static Specification<AuditLogEntity> getLogs(AuditLogFilterRequest request) {
        return (Root<AuditLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                String keyword = "%" + request.getSearch().toLowerCase() + "%";
                Predicate byUserName = cb.like(cb.lower(root.get("userName")), keyword);
                Predicate byEmail = cb.like(cb.lower(root.get("email")), keyword);
                Predicate byMerchant = cb.like(cb.lower(root.get("merchantName")), keyword);
                predicates.add(cb.or(byUserName, byEmail, byMerchant));
            }

            if (request.getUserRole() != null && !request.getUserRole().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("userRole")), request.getUserRole().toLowerCase()));
            }

            if (request.getEvent() != null && !request.getEvent().name().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("event")), request.getEvent().toString().toLowerCase()));
            }

            if (request.getStartDate() != null && request.getEndDate() != null) {
                LocalDateTime from = request.getStartDate().atStartOfDay();
                LocalDateTime to = request.getEndDate().atTime(23, 59, 59);
                predicates.add(cb.between(root.get("time"), from, to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
