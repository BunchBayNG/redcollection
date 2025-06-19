package com.bbng.dao.microservices.vacctgen.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class ApplicationProperties {
    private int invoicePoolSize;
    private int maxPoolSize;
    private int maxAssignable;
    private String defaultPrefix;
    private int provisionSingleUpdatePoolSize;
}
