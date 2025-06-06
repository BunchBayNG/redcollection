package com.bbng.dao.microservices.auth.auditlog.repository;


import com.bbng.dao.microservices.auth.auditlog.entities.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuditLogRepository extends JpaRepository<AuditLogEntity, String>, JpaSpecificationExecutor<AuditLogEntity> {
}
