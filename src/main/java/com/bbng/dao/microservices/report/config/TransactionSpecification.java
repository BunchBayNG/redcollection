package com.bbng.dao.microservices.report.config;

import com.bbng.dao.microservices.report.entity.TransactionEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<TransactionEntity> getTransactions(String search, String merchantOrgId, String status, LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                Predicate byTransactionId = cb.like(cb.lower(root.get("transactionId")), "%" + search.toLowerCase() + "%");
                Predicate byMerchantOrgId = cb.like(cb.lower(root.get("merchantOrgId")), "%" + search.toLowerCase() + "%");
                Predicate byMerchantName = cb.like(cb.lower(root.get("merchantName")), "%" + search.toLowerCase() + "%");
                Predicate byVNUBAN = cb.like(cb.lower(root.get("vnuban")), "%" + search.toLowerCase() + "%");
                predicates.add(cb.or(byTransactionId, byMerchantOrgId, byMerchantName, byVNUBAN));
            }

            if (merchantOrgId != null && !merchantOrgId.isEmpty()) {
                Predicate byMerchantName = cb.like(cb.lower(root.get("merchantOrgId")), "%" + merchantOrgId.toLowerCase() + "%");
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