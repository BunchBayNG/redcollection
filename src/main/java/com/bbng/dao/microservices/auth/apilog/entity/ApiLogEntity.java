package com.bbng.dao.microservices.auth.apilog.entity;

import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ApiLogEntity extends BaseEntity {


    @Id
    private String id;

    private String merchantPrefix;

    private LocalDateTime requestTimestamp;

    private LocalDateTime responseTimestamp;

    private String service;

    private int responseStatus;


    @PrePersist
    protected void onPrePersist() {
        if (this.id == null) {
            ULID ulid = new ULID();
            this.id = "APILOG-" + ulid.nextULID().substring(9);
        }
    }

}
