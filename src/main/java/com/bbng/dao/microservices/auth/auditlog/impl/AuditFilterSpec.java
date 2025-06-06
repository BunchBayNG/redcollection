package com.bbng.dao.microservices.auth.auditlog.impl;


import com.bbng.dao.microservices.auth.auditlog.entities.AuditLogEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuditFilterSpec {
    public Specification<AuditLogEntity> createdWithinDateRange(Optional<Instant> startDate, Optional<Instant> endDate) {
        return (root, query, criteriaBuilder) ->
                startDate.flatMap(startOfRange ->
                                endDate.map(endOfRange ->
                                        criteriaBuilder.between(root.get("dateTimeStamp"), startOfRange, endOfRange)))
                        .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<AuditLogEntity> findById(Optional<String> cohortId) {
        return (root, query, criteriaBuilder) ->
                cohortId.map(id -> criteriaBuilder.equal(root.get("id"), id))
                        .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<AuditLogEntity> findByUserId(Optional<String> cohortId) {
        return (root, query, criteriaBuilder) ->
                cohortId.map(id -> criteriaBuilder.equal(root.get("userId"), id))
                        .orElseGet(criteriaBuilder::conjunction);
    }


    public Specification<AuditLogEntity> containsUserName(Optional<String> organizationName) {
        return (root, query, criteriaBuilder) -> organizationName
                .map(param ->
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("userName")
                                ), "%" + param.toLowerCase() + "%"
                        ))
                .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<AuditLogEntity> findByMerchantId(Optional<String> cohortId) {
        return (root, query, criteriaBuilder) ->
                cohortId.map(id -> criteriaBuilder.equal(root.get("merchantId"), id))
                        .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<AuditLogEntity> containsMerchantName(Optional<String> organizationName) {
        return (root, query, criteriaBuilder) -> organizationName
                .map(param ->
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("merchantName")
                                ), "%" + param.toLowerCase() + "%"
                        ))
                .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<AuditLogEntity> findByUserType(Optional<String> customerId) {
        return (root, query, criteriaBuilder) ->
                customerId.map(id -> criteriaBuilder.equal(root.get("usertype"), id))
                        .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<AuditLogEntity> filterTransactionData(
            Optional<Instant> dateBegin,
            Optional<Instant> dateEnd,
            Optional<String> id,
            Optional<String> userId,
            Optional<String> userName,
            Optional<String> merchantId,
            Optional<String> merchantName,
            Optional<String> usertype
            ) {

        return Specification
                .where(findById(id))
                .and(findByUserId(userId))
                .and(containsUserName(userName))
                .and(findByMerchantId(merchantId))
                .and(containsMerchantName(merchantName))
                .and(findByUserType(usertype))
                .and(createdWithinDateRange(dateBegin, dateEnd));
    }
}
