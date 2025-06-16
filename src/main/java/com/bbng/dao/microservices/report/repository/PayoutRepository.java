package com.bbng.dao.microservices.report.repository;

import com.bbng.dao.microservices.report.entity.PayoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


public interface PayoutRepository extends JpaRepository<PayoutEntity, Long>, JpaSpecificationExecutor<PayoutEntity> {
}
