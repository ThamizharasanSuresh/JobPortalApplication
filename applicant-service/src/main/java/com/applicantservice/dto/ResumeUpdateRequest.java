package com.applicantservice.dto;

import lombok.Data;

@Data
public class ResumeUpdateRequest {
    private String skills;
    private String education;
    private String experience;
}
