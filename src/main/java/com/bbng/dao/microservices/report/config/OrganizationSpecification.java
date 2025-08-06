package com.bbng.dao.microservices.report.config;

import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrganizationSpecification {


    public static Specification<OrganizationEntity> getOrganizations(String search, String status,LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                Predicate byMerchantName = cb.like(cb.lower(root.get("organizationName")), "%" + search.toLowerCase() + "%");
                predicates.add(cb.or( byMerchantName));
            }
/*
//            if (merchantOrgId != null && !merchantOrgId.isEmpty()) {
//                Predicate byMerchantName = cb.like(cb.lower(root.get("merchantOrgId")), "%" + merchantOrgId.toLowerCase() + "%");
//                predicates.add(cb.or( byMerchantName));
//            }

 */

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
