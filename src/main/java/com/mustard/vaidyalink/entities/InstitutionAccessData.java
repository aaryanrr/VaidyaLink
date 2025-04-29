package com.mustard.vaidyalink.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "institution_access_db")
public class InstitutionAccessData {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "access_request_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID accessRequestId;

    @Column(name = "institution_registration_number", nullable = false)
    private String institutionRegistrationNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
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

    @Column(name = "stored_at", nullable = false)
    private LocalDateTime storedAt = LocalDateTime.now();
}
