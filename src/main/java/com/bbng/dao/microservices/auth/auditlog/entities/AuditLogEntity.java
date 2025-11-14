package com.bbng.dao.microservices.auth.auditlog.entities;

import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class AuditLogEntity  extends BaseEntity {

    @Id
    private String id;

    private String userId;
    private String userName;
    private String email;
    private String userRole;
    private String userIpAddress;
    private String merchantName;
    private String merchantOrganization;
    private String merchantOrgId;
    private String event;
    private String userType;
    private String description;



    @PrePersist
    protected void onPrePersist() {
        if (this.id == null) {
            ULID ulid = new ULID();
            this.id = "AUD-" + ulid.nextULID().substring(12);
        }
    }
}
