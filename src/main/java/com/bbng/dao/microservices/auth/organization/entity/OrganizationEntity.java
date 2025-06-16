package com.bbng.dao.microservices.auth.organization.entity;


import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class OrganizationEntity extends BaseEntity {

    @Id
    private String id;
    private String organizationName;
    private String contactFirstName;
    private String contactLastName;
    private String contactEmail;
    private String contactPhoneNumber;
    private String registeredBVN;
    private String businessLogoUrl;
    private String merchantAdminId;
    private String productPrefix;
    private String orgStatus;
    private String settlementAccountName;
    private String settlementAccountNumber;
    private String settlementBankName;
    private String settlementAccountStatus;

    @Enumerated(EnumType.STRING)

    @PrePersist
    protected void onPrePersist() {
        if (this.id == null) {
            ULID ulid = new ULID();
            this.id = "ORG-" + ulid.nextULID().substring(12);
        }
    }

}

