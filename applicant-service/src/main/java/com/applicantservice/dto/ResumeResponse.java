package com.applicantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResumeResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private String filePath;
    private String skills;
    private String education;
    private String experience;
    private Long applicantId;
}
