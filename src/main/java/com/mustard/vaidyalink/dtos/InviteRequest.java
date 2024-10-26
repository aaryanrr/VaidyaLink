package com.mustard.vaidyalink.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
public class InviteRequest {
    private String name;
    private String email;
    private String aadhaar;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String address;
    private String bloodGroup;
    private String emergencyContact;
    private String allergies;
    private Double heightCm;
    private Double weightKg;

    public String getDateOfBirth() {
        return dateOfBirth != null ? dateOfBirth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }
}
