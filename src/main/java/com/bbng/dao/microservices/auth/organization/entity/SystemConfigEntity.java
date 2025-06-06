package com.bbng.dao.microservices.auth.organization.entity;


import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class SystemConfigEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean is2FA;
    private boolean isEmailSetUp;
    private boolean isSmsSetUp;
    @Column(unique = true)
    private String userId;
}
