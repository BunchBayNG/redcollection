package com.bbng.dao.microservices.auth.auditlog.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;
    @CreatedBy
    @Column()
    private String createdBy;
    @LastModifiedDate
    @Column(insertable = false)
    private Instant updatedAt;
    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;
    private boolean isDeleted = false;
}
