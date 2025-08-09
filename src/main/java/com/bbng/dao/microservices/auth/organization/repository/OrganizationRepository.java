package com.bbng.dao.microservices.auth.organization.repository;


import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long>, JpaSpecificationExecutor<OrganizationEntity> {

    @Query("select o from OrganizationEntity o where o.merchantAdminId = ?1")
    Optional<OrganizationEntity> findOrganizationByMerchantAdminId(String id);

    @Query("select (count(o) > 0) from OrganizationEntity o where o.merchantAdminId = ?1")
    boolean existsByMerchantAdminId(String merchantAdminId);

    @Query("Select o from OrganizationEntity  o where o.id = ?1")
    Optional<OrganizationEntity> findByOrganizationId(String organizationId);

    @Query("Select (count(0) > 0) from OrganizationEntity  o where o.contactEmail = ?1")
    boolean existsByEmail(String email);

    @Query("Select o from OrganizationEntity  o where o.contactEmail = ?1")
    Optional<OrganizationEntity> findByContactEmail(String email);

    @Query("select (count(o) > 0) from OrganizationEntity o where o.organizationName = ?1")
    boolean existsByOrganizationName(String organizationName);


    Optional<OrganizationEntity>  findById(String orgId);


    // Total merchants count (distinct merchantOrgId)
//    @Query("""
//        SELECT COUNT(DISTINCT t.id) FROM OrganizationEntity t
//        WHERE t.createdAt BETWEEN :startDate AND :endDate
//    """)
//    long countDistinctMerchantOrgId(
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate
//    );

    long countByCreatedAtBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );



}
