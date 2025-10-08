package com.applicantservice.dto;

import lombok.Data;

@Data
public class ApplicantRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String education;
    private String experience;
    private Long userId;
}
