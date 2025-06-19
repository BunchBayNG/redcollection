package com.bbng.dao.microservices.report.config;

import com.bbng.dao.microservices.report.dto.CustomerFilterRequestDto;
import com.bbng.dao.microservices.report.entity.CustomerEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {



    public static Specification<CustomerEntity> getCustomers(CustomerFilterRequestDto request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                Predicate bySourceAccount = cb.like(cb.lower(root.get("sourceAccount")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate byDestinationAccount = cb.like(cb.lower(root.get("destinationAccount")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate byMerchantOrgId = cb.like(cb.lower(root.get("merchantOrgId")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate byTransactionRef = cb.like(cb.lower(root.get("transactionRef")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate byReference = cb.like(cb.lower(root.get("paymentReference")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate byMerchantName = cb.like(cb.lower(root.get("merchantName")), "%" + request.getSearch().toLowerCase() + "%");
                predicates.add(cb.or(bySourceAccount, byDestinationAccount, byMerchantOrgId, byMerchantName, byTransactionRef, byReference));
            }

            if (request.getStartDate() != null && request.getEndDate() != null) {
                predicates.add(cb.between(root.get("createdAt"), request.getStartDate().atStartOfDay(), request.getEndDate().atTime(23, 59, 59)));
            }



            if (request.getMerchantOrgId() != null && !request.getMerchantOrgId().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("merchantOrgId")), request.getMerchantOrgId().toLowerCase()));
            }



            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("status")), request.getStatus().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
