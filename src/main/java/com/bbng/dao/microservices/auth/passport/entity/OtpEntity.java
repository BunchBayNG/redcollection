package com.bbng.dao.microservices.auth.passport.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String otp;

    private String email;

    private Boolean expired;

    private Boolean revoked;

    private Instant creationTime;

    private Instant expirationTime;


}
