package com.mustard.vaidyalink.entities;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "access_requests")
public class AccessRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "aadhaar_number", nullable = false)
    private String aadhaarNumber;

    @Column(name = "data_category", nullable = false)
    private String dataCategory;

    @Column(name = "time_period", nullable = false)
    private LocalDate timePeriod;

    @Column(name = "action_required", nullable = false)
    private String actionRequired;

    @Column(name = "approved", nullable = false)
    private Boolean approved = false;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt = LocalDateTime.now();
}
