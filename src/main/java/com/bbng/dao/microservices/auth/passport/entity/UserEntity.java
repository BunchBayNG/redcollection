package com.bbng.dao.microservices.auth.passport.entity;


import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import com.bbng.dao.microservices.auth.passport.enums.AcctStatus;
import com.bbng.dao.microservices.auth.passport.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class UserEntity extends BaseEntity {

    @Id
    private String id;
    private String email;
    private String firstName;
    private String userName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private UserType usertype;
    private String password;
    private AcctStatus acctStatus;
    private String phoneNumber;
    private Boolean isEnabled;
    @Embedded
    private String logoUrl;
    private boolean isInvitedUser;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<RoleEntity> roleEntities = new HashSet<>();

    @PrePersist
    protected void onPrePersist() {
        if (this.id == null) {
            ULID ulid = new ULID();
            this.id = "USR-" + ulid.nextULID().substring(12);
        }
    }
}
