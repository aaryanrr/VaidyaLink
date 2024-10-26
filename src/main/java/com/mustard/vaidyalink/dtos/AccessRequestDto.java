package com.mustard.vaidyalink.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AccessRequestDto {
    private String aadhaarNumber;
    private String dataCategory;
    private LocalDate timePeriod;
    private String actionRequired;
}
