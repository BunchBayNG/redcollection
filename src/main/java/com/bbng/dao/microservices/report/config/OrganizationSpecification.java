package com.bbng.dao.microservices.report.config;

import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.report.dto.OrgFilterRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrganizationSpecification {


    public static Specification<OrganizationEntity> getOrganizations(OrgFilterRequestDto request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                Predicate byMerchantName = cb.like(cb.lower(root.get("merchantName")), "%" + request.getSearch().toLowerCase() + "%");
                predicates.add(cb.or( byMerchantName));
            }


            if (request.getStartDate() != null && request.getEndDate() != null) {
                predicates.add(cb.between(root.get("createdAt"), request.getStartDate().atStartOfDay(), request.getEndDate().atTime(23, 59, 59)));
            }

            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("status")), request.getStatus().toLowerCase()));
            }



            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
