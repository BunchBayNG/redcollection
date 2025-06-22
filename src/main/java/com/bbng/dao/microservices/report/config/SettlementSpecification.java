package com.bbng.dao.microservices.report.config;

import com.bbng.dao.microservices.report.entity.SettlementEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SettlementSpecification {



    public static Specification<SettlementEntity> getSettlements(String search, String merchantOrgId, String status, LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                Predicate bySourceAccount = cb.like(cb.lower(root.get("sourceAccount")), "%" + search.toLowerCase() + "%");
                Predicate byDestinationAccount = cb.like(cb.lower(root.get("destinationAccount")), "%" + search.toLowerCase() + "%");
                Predicate byMerchantOrgId = cb.like(cb.lower(root.get("merchantOrgId")), "%" + search.toLowerCase() + "%");
                Predicate byTransactionRef = cb.like(cb.lower(root.get("transactionRef")), "%" + search.toLowerCase() + "%");
                Predicate byReference = cb.like(cb.lower(root.get("reference")), "%" + search.toLowerCase() + "%");
                Predicate byMerchantName = cb.like(cb.lower(root.get("merchantName")), "%" + search.toLowerCase() + "%");
                predicates.add(cb.or(bySourceAccount, byDestinationAccount, byMerchantOrgId, byMerchantName, byTransactionRef, byReference));
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
