package com.bbng.dao.microservices.auth.passport.entity;


import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "permission_entity")
public class PermissionEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private Set<RoleEntity> roles;
}
