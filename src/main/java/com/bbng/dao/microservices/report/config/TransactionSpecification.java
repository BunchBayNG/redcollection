package com.bbng.dao.microservices.report.config;

import com.bbng.dao.microservices.report.dto.TransactionFilterRequestDto;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<TransactionEntity> getTransactions(TransactionFilterRequestDto request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                Predicate byTransactionId = cb.like(cb.lower(root.get("transactionId")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate byMerchantOrgId = cb.like(cb.lower(root.get("merchantOrgId")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate byMerchantName = cb.like(cb.lower(root.get("merchantName")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate byVNUBAN = cb.like(cb.lower(root.get("vnuban")), "%" + request.getSearch().toLowerCase() + "%");
                predicates.add(cb.or(byTransactionId, byMerchantOrgId, byMerchantName, byVNUBAN));
            }

            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("status")), request.getStatus().toLowerCase()));
            }

            if (request.getStartDate() != null && request.getEndDate() != null) {
                predicates.add(cb.between(root.get("createdAt"), request.getStartDate().atStartOfDay(), request.getEndDate().atTime(23, 59, 59)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}