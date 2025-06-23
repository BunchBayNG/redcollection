package com.bbng.dao.microservices.report.repository;

import com.bbng.dao.microservices.report.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>, JpaSpecificationExecutor<TransactionEntity> {


    // Total merchants count (distinct merchantOrgId)
    @Query("""
        SELECT COUNT(DISTINCT t.merchantOrgId) FROM TransactionEntity t
        WHERE t.createdAt BETWEEN :startDate AND :endDate
    """)
    long countDistinctMerchantOrgId(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // Top merchants by volume
//    @Query("""
//        SELECT new  com.bbng.dao.microservices.report.dto.TopMerchantDTO(t.merchantOrgId, t.merchantName, COALESCE(SUM(t.amount), 0))
//        FROM TransactionEntity t
//        WHERE t.createdAt BETWEEN:startDate AND:endDate
//        GROUP BY t.merchantOrgId
//        ORDER BY SUM(t.amount) DESC
//    """)
//    List<TopMerchantDTO> findTopMerchantsByVolume(
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate,
//            Pageable pageable
//    );


    @Query(value = """
    SELECT t.merchant_org_id AS merchantOrgId,
           SUM(t.amount) AS totalVolume
    FROM transaction_entity t
    WHERE t.created_at BETWEEN :startDate AND :endDate
    GROUP BY t.merchant_org_id
    ORDER BY totalVolume DESC
    LIMIT :topN
""", nativeQuery = true)
    List<Object[]> findTopMerchantsByVolume(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("topN") int topN
    );


    // Total transaction count
    @Query("""
        SELECT COUNT(t) FROM TransactionEntity t
        WHERE (:merchantOrgId IS NULL OR t.merchantOrgId = :merchantOrgId)
        AND t.createdAt BETWEEN :startDate AND :endDate
    """)
    long countByCreatedAtBetween(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // Count by status
    @Query("""
        SELECT COUNT(t) FROM TransactionEntity t
        WHERE (:merchantOrgId IS NULL OR t.merchantOrgId = :merchantOrgId)
        AND t.status = :status
        AND t.createdAt BETWEEN :startDate AND :endDate
    """)
    long countByStatusAndCreatedAtBetween(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // Sum successful amount
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0) FROM TransactionEntity t
        WHERE (:merchantOrgId IS NULL OR t.merchantOrgId = :merchantOrgId)
        AND t.status = :status
        AND t.createdAt BETWEEN :startDate AND :endDate
    """)
    BigDecimal sumAmountByStatus(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

//    // Volume per period
//    @Query("""
//        SELECT new com.bbng.dao.microservices.report.dto.ChartPointDTO(
//            FUNCTION('DATE_FORMAT', t.createdAt, :pattern),
//            SUM(t.amount)
//        )
//        FROM TransactionEntity t
//        WHERE (:merchantOrgId IS NULL OR t.merchantOrgId = :merchantOrgId)
//        AND t.status = 'SUCCESS'
//        AND t.createdAt BETWEEN :startDate AND :endDate
//        GROUP BY FUNCTION('DATE_FORMAT', t.createdAt, :pattern)
//        ORDER BY FUNCTION('DATE_FORMAT', t.createdAt, :pattern)
//    """)
//    List<ChartPointDTO> groupSuccessfulTransactionVolumeByPeriod(
//            @Param("merchantOrgId") Long merchantOrgId,
//            @Param("pattern") String pattern,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate
//    );
//
//    // Count per period
//    @Query("""
//        SELECT new com.bbng.dao.microservices.report.dto.ChartPointDTO(
//            FUNCTION('DATE_FORMAT', t.createdAt, :pattern),
//            COUNT(t)
//        )
//        FROM TransactionEntity t
//        WHERE (:merchantOrgId IS NULL OR t.merchantOrgId = :merchantOrgId)
//        AND t.status = 'SUCCESS'
//        AND t.createdAt BETWEEN :startDate AND :endDate
//        GROUP BY FUNCTION('DATE_FORMAT', t.createdAt, :pattern)
//        ORDER BY FUNCTION('DATE_FORMAT', t.createdAt, :pattern)
//    """)
//    List<ChartPointDTO> groupSuccessfulTransactionCountByPeriod(
//            @Param("merchantOrgId") Long merchantOrgId,
//            @Param("pattern") String pattern,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate
//    );

    // Volume per period

    @Query(value = """
    SELECT DATE_FORMAT(t.created_at, :pattern) AS period,
           SUM(t.amount) AS value
    FROM transaction_entity t
    WHERE (:merchantOrgId IS NULL OR t.merchant_org_id = :merchantOrgId)
      AND t.status = 'SUCCESS'
      AND t.created_at BETWEEN :startDate AND :endDate
    GROUP BY DATE_FORMAT(t.created_at, :pattern)
    ORDER BY DATE_FORMAT(t.created_at, :pattern)
""", nativeQuery = true)
    List<Object[]> groupSuccessfulTransactionVolumeByPeriod(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("pattern") String pattern,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query(value = """
    SELECT DATE_FORMAT(t.created_at, :pattern) AS period,
           COUNT(t.id) AS value
    FROM transaction_entity t
    WHERE (:merchantOrgId IS NULL OR t.merchant_org_id = :merchantOrgId)
      AND t.status = 'SUCCESS'
      AND t.created_at BETWEEN :startDate AND :endDate
    GROUP BY DATE_FORMAT(t.created_at, :pattern)
    ORDER BY DATE_FORMAT(t.created_at, :pattern)
""", nativeQuery = true)
    List<Object[]> groupSuccessfulTransactionCountByPeriod(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("pattern") String pattern,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
