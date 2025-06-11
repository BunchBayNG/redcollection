package com.bbng.dao.microservices.report.repository;

import com.bbng.dao.microservices.report.entity.VnubanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VnubanRepository extends JpaRepository<VnubanEntity, Long>, JpaSpecificationExecutor<VnubanEntity> {
}
