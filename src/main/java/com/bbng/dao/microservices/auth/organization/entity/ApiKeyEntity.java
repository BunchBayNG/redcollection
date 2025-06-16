package com.bbng.dao.microservices.auth.organization.entity;


import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApiKeyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    @JsonIgnore
    private String testKey;
    @JsonIgnore
    private String liveKey;
    private String dateCreated;
}
