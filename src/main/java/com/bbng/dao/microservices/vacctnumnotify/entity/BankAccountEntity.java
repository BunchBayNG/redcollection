package com.bbng.dao.microservices.vacctnumnotify.entity;

import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
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
public class BankAccountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String bankAccountName;
    private Long bankAccountNumber;
    private BankCategory bankCategory;

    // other fields

}
