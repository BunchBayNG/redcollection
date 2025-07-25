package com.bbng.dao.microservices.auth.apilog.repository;

import com.bbng.dao.microservices.auth.apilog.entity.ApiLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApiLogRepository extends JpaRepository<ApiLogEntity, Long>, JpaSpecificationExecutor<ApiLogEntity> {
}
