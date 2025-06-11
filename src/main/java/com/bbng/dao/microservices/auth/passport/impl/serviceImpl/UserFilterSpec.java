package com.bbng.dao.microservices.auth.passport.impl.serviceImpl;

import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserFilterSpec {
    public Specification<UserEntity> createdWithinDateRange(Optional<Instant> startDate, Optional<Instant> endDate) {
        return (root, query, criteriaBuilder) ->
                startDate.flatMap(startOfRange ->
                                endDate.map(endOfRange ->
                                        criteriaBuilder.between(root.get("createdAt"), startOfRange, endOfRange)))
                        .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<UserEntity> findById(Optional<String> cohortId) {
        return (root, query, criteriaBuilder) ->
                cohortId.map(id -> criteriaBuilder.equal(root.get("id"), id))
                        .orElseGet(criteriaBuilder::conjunction);
    }


    public Specification<UserEntity> containsEmail(Optional<String> organizationName) {
        return (root, query, criteriaBuilder) -> organizationName
                .map(param ->
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("email")
                                ), "%" + param.toLowerCase() + "%"
                        ))
                .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<UserEntity> containsFirstName(Optional<String> organizationName) {
        return (root, query, criteriaBuilder) -> organizationName
                .map(param ->
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("firstName")
                                ), "%" + param.toLowerCase() + "%"
                        ))
                .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<UserEntity> containsLastName(Optional<String> organizationName) {
        return (root, query, criteriaBuilder) -> organizationName
                .map(param ->
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("lastName")
                                ), "%" + param.toLowerCase() + "%"
                        ))
                .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<UserEntity> findByUserType(Optional<String> customerId) {
        return (root, query, criteriaBuilder) ->
                customerId.map(id -> criteriaBuilder.equal(root.get("usertype"), id))
                        .orElseGet(criteriaBuilder::conjunction);
    }

    public Specification<UserEntity> filterTransactionData(
            Optional<Instant> dateBegin,
            Optional<Instant> dateEnd,
            Optional<String> id,
            Optional<String> email,
            Optional<String> firstName,
            Optional<String> lastName,
            Optional<String> usertype
    ) {

        return Specification
                .where(findById(id))
                .and(containsEmail(email))
                .and(createdWithinDateRange(dateBegin, dateEnd))
                .and(containsFirstName(firstName))
                .and(containsLastName(lastName))
                .and(findByUserType(usertype));
    }
}
