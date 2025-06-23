package com.bbng.dao.microservices.auth.passport.entity;


import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import com.bbng.dao.microservices.auth.passport.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class TokenEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokenReport")
    @SequenceGenerator(name = "tokenReport", sequenceName = "token_report")
    private Long id;
    @Column(name = "token", length = 50000)
    private String token;
    private TokenType tokenType;
    private Boolean expired;
    private Boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    private UserEntity userEntity;
}
