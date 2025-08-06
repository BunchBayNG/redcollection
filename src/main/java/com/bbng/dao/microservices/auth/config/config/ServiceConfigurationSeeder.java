package com.bbng.dao.microservices.auth.config.config;

import com.bbng.dao.microservices.auth.config.entity.ServiceConfigEntity;
import com.bbng.dao.microservices.auth.config.enums.FeeType;
import com.bbng.dao.microservices.auth.config.enums.ServiceType;
import com.bbng.dao.microservices.auth.config.repository.ServiceConfigRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Optional;

@Configuration
public class ServiceConfigurationSeeder {

    @Bean
    CommandLineRunner seedServiceConfigurations(ServiceConfigRepository repository) {
        return args -> {
            seedIfNotExists(repository, ServiceType.OPEN_ACCOUNT, BigDecimal.ZERO, FeeType.FLAT, null);
            seedIfNotExists(repository, ServiceType.VIRTUAL_TRANSACTION, new BigDecimal("0.5"), FeeType.PERCENTAGE_WITH_CAP, new BigDecimal("500"));
            seedIfNotExists(repository, ServiceType.MERCHANT_SETUP, BigDecimal.ZERO, FeeType.FLAT, null);
        };
    }

    private void seedIfNotExists(ServiceConfigRepository repository,
                                 ServiceType serviceType,
                                 BigDecimal fee,
                                 FeeType feeType,
                                 BigDecimal cap) {
        Optional<ServiceConfigEntity> existing = repository.findByServiceType(serviceType);
        if (existing.isEmpty()) {
            ServiceConfigEntity config = new ServiceConfigEntity();
            config.setServiceType(serviceType);
            config.setFee(fee);
            config.setFeeType(feeType);
            config.setCap(cap);
            repository.save(config);
            System.out.println("Seeded service configuration for: " + serviceType);
        }
    }
}
