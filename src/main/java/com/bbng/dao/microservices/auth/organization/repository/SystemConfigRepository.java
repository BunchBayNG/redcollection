package com.bbng.dao.microservices.auth.organization.repository;

import com.bbng.dao.microservices.auth.organization.entity.SystemConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SystemConfigRepository extends JpaRepository<SystemConfigEntity, Long> {

//    @Query("Select s from SystemConfigEntity s where s.userId = ?1")
//    Optional<SystemConfigEntity> findByUserId(String userId);

    SystemConfigEntity findByIdIs(Long Id );

}
