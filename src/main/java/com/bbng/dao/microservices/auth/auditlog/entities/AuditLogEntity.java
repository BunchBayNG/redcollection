package com.bbng.dao.microservices.auth.auditlog.entities;

import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class AuditLogEntity {

    @Id
    private String id;
    private String event;
    private String userId;
    private String userName;
    private String merchantId;
    private String merchantName;
    private String userType;
    private String description;
    private Instant dateTimeStamp;
    private boolean isDeleted;
    private boolean succeeded;

    @PrePersist
    protected void onPrePersist() {
        if (this.id == null) {
            ULID ulid = new ULID();
            this.id = "AUD-" + ulid.nextULID().substring(12);
        }
        if (this.dateTimeStamp == null) {
            this.dateTimeStamp = Instant.now();
        }
    }
}
