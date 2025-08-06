package com.bbng.dao.microservices.auth.auditlog.impl;

import com.bbng.dao.microservices.auth.auditlog.entities.AuditLogEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuditLogSpecification {

    public static Specification<AuditLogEntity> getLogs(String search, String merchantOrgId, LocalDate startDate, LocalDate endDate) {
        return (Root<AuditLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                Predicate byUsername = cb.like(cb.lower(root.get("userName")), "%" + search.toLowerCase() + "%");
                Predicate byMerchantOrgId = cb.like(cb.lower(root.get("merchantOrgId")), "%" + search.toLowerCase() + "%");
                Predicate byMerchantName = cb.like(cb.lower(root.get("merchantName")), "%" + search.toLowerCase() + "%");
                Predicate byMerchantOrganization = cb.like(cb.lower(root.get("merchantOrganization")), "%" + search.toLowerCase() + "%");
                Predicate byEvent = cb.like(cb.lower(root.get("event")), "%" + search.toLowerCase() + "%");
                predicates.add(cb.or(byUsername, byMerchantOrgId,byMerchantOrganization, byMerchantName, byEvent));
            }

            if (merchantOrgId != null && !merchantOrgId.isEmpty()) {
                Predicate byMerchantName = cb.like(cb.lower(root.get("merchantOrgId")), "%" + merchantOrgId.toLowerCase() + "%");
                predicates.add(cb.or( byMerchantName));
            }


            if (startDate != null &&endDate != null) {
                predicates.add(cb.between(root.get("createdAt"), startDate.atStartOfDay(),endDate.atTime(23, 59, 59)));
            }
//
//            if (status != null && !status.isEmpty()) {
//                predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
//            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
