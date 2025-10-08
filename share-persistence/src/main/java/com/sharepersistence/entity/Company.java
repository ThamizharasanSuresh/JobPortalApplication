package com.sharepersistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String industry;
    private String location;
    private String website;
    private String logoUrl;

    private Long userId;// Link to Auth Service user
}
