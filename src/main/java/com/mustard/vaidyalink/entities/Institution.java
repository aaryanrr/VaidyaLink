package com.mustard.vaidyalink.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "institutions", indexes = {
        @Index(name = "idx_institutions_email", columnList = "email", unique = true)
})
public class Institution {
    @Id
    @Column(name = "registration_number", nullable = false, length = 20)
    private String registrationNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "license_file_path", nullable = false)
    private String licenseFilePath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
