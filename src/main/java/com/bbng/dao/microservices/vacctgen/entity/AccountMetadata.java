package com.bbng.dao.microservices.vacctgen.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMetadata {

    @Id
    private String prefix;

    @CreationTimestamp
    private LocalDateTime dateCreated;

    private Integer quantity;

    private Integer reportedCollision;
}
