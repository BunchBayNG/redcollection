package com.bbng.dao.microservices.report.repository;

import com.bbng.dao.microservices.vacctgen.entity.ProvisionedAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface VnubanRepository extends JpaRepository<ProvisionedAccount, Long>, JpaSpecificationExecutor<ProvisionedAccount> {


    @Query("""
    SELECT COUNT(v) FROM ProvisionedAccount v
    WHERE (:merchantOrgId IS NULL OR v.merchantOrgId = :merchantOrgId)
    AND v.provisionDate BETWEEN :startDate AND :endDate
    AND v.mode = 'OPEN'
""")
    long countByGeneratedAtBetweenForOpen(
            @Param("merchantOrgId") String merchantOrgId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query("""
    SELECT COUNT(v) FROM ProvisionedAccount v
    WHERE (:merchantOrgId IS NULL OR v.merchantOrgId = :merchantOrgId)
    AND v.provisionDate BETWEEN :startDate AND :endDate
    AND v.mode = 'CLOSED'
""")
    long countByGeneratedAtBetweenForClosed(
            @Param("merchantOrgId") String merchantOrgId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT COUNT(v) FROM ProvisionedAccount v
           WHERE (:merchantOrgId IS NULL OR v.merchantOrgId = :merchantOrgId)
           AND v.provisionDate BETWEEN :startDate AND :endDate
""")
    long countByProvisionDate(
            @Param("merchantOrgId") String merchantOrgId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

//    @Query("""
//        SELECT COUNT(v) FROM ProvisionedAccount v
//        WHERE (:merchantOrgId IS NULL OR v.merchantOrgId = :merchantOrgId)
//        AND v.provisionDate BETWEEN :startDate AND :endDate
//    """)
//    long countByTypeAndGeneratedAtBetween(
//            @Param("merchantOrgId") String merchantOrgId,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate
//    );

    @Query(value = """
    SELECT DATE_FORMAT(v.created_at, :pattern) AS period,
           COUNT(v.id) AS value
    FROM vnuban_entity v
    WHERE (:merchantOrgId IS NULL OR v.merchant_org_id = :merchantOrgId)
      AND v.created_at BETWEEN :startDate AND :endDate
    GROUP BY DATE_FORMAT(v.created_at, :pattern)
    ORDER BY DATE_FORMAT(v.created_at, :pattern)
""", nativeQuery = true)
    List<Object[]> groupGeneratedVnubansByPeriod(
            @Param("merchantOrgId") String merchantOrgId,
            @Param("pattern") String pattern,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
