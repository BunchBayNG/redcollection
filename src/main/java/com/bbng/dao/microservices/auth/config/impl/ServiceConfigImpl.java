package com.bbng.dao.microservices.auth.config.impl;

import com.bbng.dao.microservices.auth.config.dto.request.ServiceConfigRequest;
import com.bbng.dao.microservices.auth.config.entity.ServiceConfigEntity;
import com.bbng.dao.microservices.auth.config.enums.ServiceType;
import com.bbng.dao.microservices.auth.config.repository.ServiceConfigRepository;
import com.bbng.dao.microservices.auth.config.service.ServiceConfigService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceConfigImpl implements ServiceConfigService {


    private final ServiceConfigRepository repository;


    public ServiceConfigImpl(ServiceConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceConfigEntity saveOrUpdate(ServiceConfigRequest request) {

        ServiceConfigEntity config = repository.findByServiceType(request.getServiceType())
                .orElse(new ServiceConfigEntity());

        config.setServiceType(request.getServiceType());
        config.setFee(request.getFee());
        config.setFeeType(request.getFeeType());
        config.setCap(request.getCap());

        return repository.save(config);
    }

    @Override
    public List<ServiceConfigEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ServiceConfigEntity> getByServiceType(ServiceType serviceType) {
        return repository.findByServiceType(serviceType);
    }
}
