package com.bbng.dao.microservices.vacctgen.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String value;

    @NonNull
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        PROVISIONED, RESERVED, FREE, SUSPENDED
    }
}
