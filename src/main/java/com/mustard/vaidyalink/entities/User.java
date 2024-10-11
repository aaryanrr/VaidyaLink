package com.mustard.vaidyalink.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_aadhaar_hash", columnList = "aadhaar_number_hash", unique = true)
})
public class User {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @Column(name = "aadhaar_number_hash", nullable = false, unique = true)
    private String aadhaarNumberHash;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
