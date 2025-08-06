package com.bbng.dao.microservices.auth.config.repository;

import com.bbng.dao.microservices.auth.config.entity.ServiceConfigEntity;
import com.bbng.dao.microservices.auth.config.enums.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceConfigRepository  extends JpaRepository<ServiceConfigEntity, Long> {


    Optional<ServiceConfigEntity> findByServiceType(ServiceType serviceType);
}
