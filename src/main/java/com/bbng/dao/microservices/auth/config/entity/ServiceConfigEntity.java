package com.bbng.dao.microservices.auth.config.entity;


import com.bbng.dao.microservices.auth.config.enums.FeeType;
import com.bbng.dao.microservices.auth.config.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data

public class ServiceConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ServiceType serviceType;

    private BigDecimal fee;

    @Enumerated(EnumType.STRING)
    private FeeType feeType;

    private BigDecimal cap;


}
