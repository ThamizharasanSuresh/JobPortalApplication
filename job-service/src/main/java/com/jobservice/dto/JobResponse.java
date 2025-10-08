package com.jobservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String employmentType;
    private Double salary;
    private String skills;
    private String companyName;
}

