package com.bbng.dao.microservices.auth.apilog.impl;

import com.bbng.dao.microservices.auth.apilog.dto.request.ApiLogFilterRequest;
import com.bbng.dao.microservices.auth.apilog.entity.ApiLogEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiLogSpecification {

    public static Specification<ApiLogEntity> getLogs(ApiLogFilterRequest request) {
        return (Root<ApiLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                String keyword = "%" + request.getSearch().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("merchantPrefix")), keyword));
            }

            if (request.getResponseStatus() != null) {
                predicates.add(cb.equal(root.get("responseStatus"), request.getResponseStatus()));
            }

            if (request.getStartDate() != null && request.getEndDate() != null) {
                LocalDateTime from = request.getStartDate().atStartOfDay();
                LocalDateTime to = request.getEndDate().atTime(23, 59, 59);
                predicates.add(cb.between(root.get("timestamp"), from, to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
