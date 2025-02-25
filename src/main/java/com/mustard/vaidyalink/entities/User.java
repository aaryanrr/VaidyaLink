package com.mustard.vaidyalink.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_aadhaar_hash", columnList = "aadhaar_number_hash", unique = true),
        @Index(name = "idx_users_email", columnList = "email", unique = true)
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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "blood_group", nullable = false)
    private String bloodGroup;

    @Column(name = "emergency_contact", nullable = false)
    private String emergencyContact;

    @Column(name = "allergies", nullable = false)
    private String allergies;

    @Column(name = "height_cm", precision = 5, nullable = false)
    private Double heightCm;

    @Column(name = "weight_kg", precision = 5, nullable = false)
    private Double weightKg;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
