package com.bbng.dao.microservices.report.repository;

import com.bbng.dao.microservices.report.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SettlementRepository extends JpaRepository<SettlementEntity, Long>, JpaSpecificationExecutor<SettlementEntity> {
}
