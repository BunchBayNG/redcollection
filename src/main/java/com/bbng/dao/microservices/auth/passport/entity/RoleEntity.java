package com.bbng.dao.microservices.auth.passport.entity;


import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@ToString
@Table(name = "role_entity")
public class RoleEntity extends BaseEntity {
    @Id
    private String id;
    private String roleName;
    private String description;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> permissions = new HashSet<>();

    @PrePersist
    protected void onPrePersist() {
        if (this.id == null) {
            ULID ulid = new ULID();
            this.id = "ROL-" + ulid.nextULID().substring(12);
        }
    }

}
