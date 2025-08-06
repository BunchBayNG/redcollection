package com.bbng.dao.microservices.auth.config.service;

import com.bbng.dao.microservices.auth.config.dto.request.ServiceConfigRequest;
import com.bbng.dao.microservices.auth.config.entity.ServiceConfigEntity;
import com.bbng.dao.microservices.auth.config.enums.ServiceType;


import java.util.List;
import java.util.Optional;


public interface ServiceConfigService {


    ServiceConfigEntity saveOrUpdate(ServiceConfigRequest request);
    List<ServiceConfigEntity> getAll();
    Optional<ServiceConfigEntity> getByServiceType(ServiceType serviceType);
}
