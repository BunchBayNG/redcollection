package com.bbng.dao.microservices.auth.apilog.impl;

import com.bbng.dao.microservices.auth.apilog.entity.ApiLogEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApiLogSpecification {

    public static Specification<ApiLogEntity> getLogs(String search, String merchantPrefix, String status, LocalDate startDate, LocalDate endDate) {
        return (Root<ApiLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                Predicate byTransactionId = cb.like(cb.lower(root.get("merchantPrefix")), "%" + search.toLowerCase() + "%");
                predicates.add(cb.or(byTransactionId));
            }

            if (merchantPrefix != null && !merchantPrefix.isEmpty()) {
                Predicate byMerchantName = cb.like(cb.lower(root.get("merchantPrefix")), "%" + merchantPrefix.toLowerCase() + "%");
                predicates.add(cb.or( byMerchantName));
            }

            if (startDate != null &&endDate != null) {
                predicates.add(cb.between(root.get("createdAt"), startDate.atStartOfDay(),endDate.atTime(23, 59, 59)));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
