package com.bbng.dao.microservices.report.repository;

import com.bbng.dao.microservices.report.entity.VnubanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;


public interface VnubanRepository extends JpaRepository<VnubanEntity, Long>, JpaSpecificationExecutor<VnubanEntity> {


    @Query("""
        SELECT COUNT(v) FROM VnubanEntity v
        WHERE (:merchantOrgId IS NULL OR v.merchantOrgId = :merchantOrgId)
        AND v.createdAt BETWEEN :startDate AND :endDate
    """)
    long countByGeneratedAtBetween(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT COUNT(v) FROM VnubanEntity v
        WHERE (:merchantOrgId IS NULL OR v.merchantOrgId = :merchantOrgId)
        AND v.vnubanType = :type
        AND v.createdAt BETWEEN :startDate AND :endDate
    """)
    long countByTypeAndGeneratedAtBetween(
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("vnubanType") String vnubanType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

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
            @Param("merchantOrgId") Long merchantOrgId,
            @Param("pattern") String pattern,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
