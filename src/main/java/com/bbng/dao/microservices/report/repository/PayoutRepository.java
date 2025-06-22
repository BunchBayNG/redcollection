package com.bbng.dao.microservices.report.repository;


import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.entity.PayoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;




public interface PayoutRepository extends JpaRepository<PayoutEntity, Long>, JpaSpecificationExecutor<PayoutEntity> {

    @Query("""
        SELECT COUNT(p) FROM PayoutEntity p
        WHERE (:merchantOrgId IS NULL OR p.merchantOrgId = :merchantOrgId)
        AND p.createdAt BETWEEN :startDate AND :endDate
    """)
    long countByCreatedAtBetween(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT COUNT(p) FROM PayoutEntity p
        WHERE (:merchantOrgId IS NULL OR p.merchantOrgId = :merchantOrgId)
        AND p.status = :status
        AND p.createdAt BETWEEN :startDate AND :endDate
    """)
    long countByStatusAndCreatedAtBetween(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0) FROM PayoutEntity p
        WHERE (:merchantOrgId IS NULL OR p.merchantOrgId = :merchantOrgId)
        AND p.status = :status
        AND p.createdAt BETWEEN :startDate AND :endDate
    """)
    BigDecimal sumAmountByStatus(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query(value = """
    SELECT DATE_FORMAT(p.created_at, :pattern) AS period,
           SUM(p.amount) AS value
    FROM payout_entity p
    WHERE (:merchantOrgId IS NULL OR p.merchant_org_id = :merchantOrgId)
      AND p.status = 'SUCCESS'
      AND p.created_at BETWEEN :startDate AND :endDate
    GROUP BY DATE_FORMAT(p.created_at, :pattern)
    ORDER BY DATE_FORMAT(p.created_at, :pattern)
""", nativeQuery = true)
    List<Object[]> groupSuccessfulPayoutVolumeByPeriod(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("pattern") String pattern,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query(value = """
    SELECT DATE_FORMAT(p.created_at, :pattern) AS period,
           COUNT(p.id) AS value
    FROM payout_entity p
    WHERE (:merchantOrgId IS NULL OR p.merchant_org_id = :merchantOrgId)
      AND p.status = 'SUCCESS'
      AND p.created_at BETWEEN :startDate AND :endDate
    GROUP BY DATE_FORMAT(p.created_at, :pattern)
    ORDER BY DATE_FORMAT(p.created_at, :pattern)
""", nativeQuery = true)
    List<Object[]> groupSuccessfulPayoutCountByPeriod(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("pattern") String pattern,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
