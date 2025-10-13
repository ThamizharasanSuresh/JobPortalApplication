package com.jobservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobRequest {
    private String title;
    private String description;
    private String location;
    private String employmentType;
    private Double salary;
    private String skills;
    private String experience;
    private Long companyId;
}
