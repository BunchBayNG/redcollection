package com.bbng.dao.microservices.auth.organization.entity;


import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import com.bbng.dao.microservices.auth.organization.enums.InvitationStatus;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class OrgStaffEntity extends BaseEntity {
    @Id
    private String id;
    private String organizationId;
    private String userId;
    private String userRole;
    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus;
    private String invitedBy;

    @PrePersist
    protected void onPrePersist() {
        if (this.id == null) {
            ULID ulid = new ULID();
            this.id = "OSF-" + ulid.nextULID().substring(12);
        }
    }

}
