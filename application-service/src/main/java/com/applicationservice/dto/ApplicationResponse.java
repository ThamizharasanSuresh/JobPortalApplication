package com.applicationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long applicantId;
    private String applicantName;
    private Long jobId;
    private String jobName;
    private String status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
