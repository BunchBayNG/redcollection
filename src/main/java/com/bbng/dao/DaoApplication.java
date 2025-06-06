package com.bbng.dao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class DaoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DaoApplication.class, args);
    }

}
