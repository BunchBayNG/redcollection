package com.bbng.dao.microservices.report.repository;

import com.bbng.dao.microservices.report.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public interface SettlementRepository extends JpaRepository<SettlementEntity, Long>, JpaSpecificationExecutor<SettlementEntity> {



    @Query("""
        SELECT COUNT(s) FROM SettlementEntity s
        WHERE (:merchantOrgId IS NULL OR s.merchantOrgId = :merchantOrgId)
        AND s.createdAt BETWEEN :startDate AND :endDate
    """)
    long countByCreatedAtBetween(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT COUNT(s) FROM SettlementEntity s
        WHERE (:merchantOrgId IS NULL OR s.merchantOrgId = :merchantOrgId)
        AND s.status = :status
        AND s.createdAt BETWEEN :startDate AND :endDate
    """)
    long countByStatusAndCreatedAtBetween(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT COALESCE(SUM(s.amount), 0) FROM SettlementEntity s
        WHERE (:merchantOrgId IS NULL OR s.merchantOrgId = :merchantOrgId)
        AND s.status = :status
        AND s.createdAt BETWEEN :startDate AND :endDate
    """)
    BigDecimal sumAmountByStatus(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

//    @Query("""
//        SELECT new com.bbng.dao.microservices.report.dto.ChartPointDTO(
//            FUNCTION('DATE_FORMAT', s.createdAt, :pattern),
//            SUM(s.amount)
//        )
//        FROM SettlementEntity s
//        WHERE (:merchantOrgId IS NULL OR s.merchantOrgId = :merchantOrgId)
//        AND s.status = 'SUCCESS'
//        AND s.createdAt BETWEEN :startDate AND :endDate
//        GROUP BY FUNCTION('DATE_FORMAT', s.createdAt, :pattern)
//        ORDER BY FUNCTION('DATE_FORMAT', s.createdAt, :pattern)
//    """)
//    List<ChartPointDTO> groupSuccessfulSettlementVolumeByPeriod(
//            @Param("merchantOrgId") Long merchantOrgId,
//            @Param("pattern") String pattern,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate
//    );
//
//    @Query("""
//        SELECT new com.bbng.dao.microservices.report.dto.ChartPointDTO(
//            FUNCTION('DATE_FORMAT', s.createdAt, :pattern),
//            COUNT(s)
//        )
//        FROM SettlementEntity s
//        WHERE (:merchantOrgId IS NULL OR s.merchantOrgId = :merchantOrgId)
//        AND s.status = 'SUCCESS'
//        AND s.createdAt BETWEEN :startDate AND :endDate
//        GROUP BY FUNCTION('DATE_FORMAT', s.createdAt, :pattern)
//        ORDER BY FUNCTION('DATE_FORMAT', s.createdAt, :pattern)
//    """)
//    List<ChartPointDTO> groupSuccessfulSettlementCountByPeriod(
//            @Param("merchantOrgId") Long merchantOrgId,
//            @Param("pattern") String pattern,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate
//    );


    @Query(value = """
    SELECT DATE_FORMAT(p.created_at, :pattern) AS period,
           SUM(p.amount) AS value
    FROM settlement_entity p
    WHERE (:merchantOrgId IS NULL OR p.merchant_org_id = :merchantOrgId)
      AND p.status = 'SUCCESS'
      AND p.created_at BETWEEN :startDate AND :endDate
    GROUP BY DATE_FORMAT(p.created_at, :pattern)
    ORDER BY DATE_FORMAT(p.created_at, :pattern)
""", nativeQuery = true)
    List<Object[]> groupSuccessfulSettlementVolumeByPeriod(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("pattern") String pattern,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query(value = """
    SELECT DATE_FORMAT(p.created_at, :pattern) AS period,
           COUNT(p.id) AS value
    FROM settlement_entity p
    WHERE (:merchantOrgId IS NULL OR p.merchant_org_id = :merchantOrgId)
      AND p.status = 'SUCCESS'
      AND p.created_at BETWEEN :startDate AND :endDate
    GROUP BY DATE_FORMAT(p.created_at, :pattern)
    ORDER BY DATE_FORMAT(p.created_at, :pattern)
""", nativeQuery = true)
    List<Object[]> groupSuccessfulSettlementCountByPeriod(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("pattern") String pattern,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
