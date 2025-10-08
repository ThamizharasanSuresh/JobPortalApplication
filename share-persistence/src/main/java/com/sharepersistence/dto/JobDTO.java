package com.sharepersistence.dto;


import lombok.Data;

@Data
public class JobDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String employmentType;
    private Double salary;
    private String skills;
    private String companyname;
}
