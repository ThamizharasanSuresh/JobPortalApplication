package com.sharepersistence.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private String filePath; // Path in server or cloud storage
    private String skills; // Parsed skills from resume
    private String education;
    private String experience;

    @ManyToOne
    @JsonBackReference("applicant-resume")
    private Applicant applicant;
}