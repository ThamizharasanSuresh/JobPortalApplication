package com.applicantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String education;
    private String experience;
    private Long userId;
    private List<ResumeResponse> resumes;
}