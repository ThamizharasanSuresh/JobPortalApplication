package com.example.companyservice.dto;


import lombok.Data;

@Data
public class CompanyRequest {
    private String name;
    private String description;
    private String industry;
    private String location;
    private String website;
    private String logoUrl;
}