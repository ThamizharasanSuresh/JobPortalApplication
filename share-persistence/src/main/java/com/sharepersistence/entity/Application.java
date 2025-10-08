package com.sharepersistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Applicant applicant;

    @ManyToOne
    private Job job;

    private String status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}