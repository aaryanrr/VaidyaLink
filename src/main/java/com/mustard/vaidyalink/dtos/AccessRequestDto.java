package com.mustard.vaidyalink.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class AccessRequestDto {
    private String aadhaarNumber;
    private String institutionName;
    private List<String> dataCategory;
    private LocalDate timePeriod;
    private List<String> actionRequired;
}
