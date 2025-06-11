package com.bbng.dao.util.email.entity;


import com.bbng.dao.microservices.auth.auditlog.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class EmailEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "email_type")
    private String emailType;
    @ElementCollection
    @CollectionTable(
            name = "email_receivers", // The name of the table to store the list
            joinColumns = @JoinColumn(name = "email_id") // The foreign key column in the table
    )
    @Column(name = "receiver_email")
    private List<String> receiver;
    @Column(name = "mailStatus")
    private String status;
    @Column(name = "sentEmailId")
    private String emailId;
    @Column(name = "queued_reason")
    private String queuedReason;
    @Column(name = "reject_reason")
    private String rejectReason;
    private String message;
    private String htmlContent;
    private String merchantId;


}
