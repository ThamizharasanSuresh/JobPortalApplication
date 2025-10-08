package com.sharepersistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private String employmentType; // Full-time, Part-time, etc.
    private Double salary;
    private String skills; // comma-separated or List<String> if using ElementCollection

    @ManyToOne
    private Company company; // Link to company

}