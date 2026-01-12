package com.osem.vpar.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vacancies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "company_name")
    private String companyName;

    private String salary;

    @Column(nullable = false, unique = true)
    private String url;

    private String dateAdded;

    private LocalDateTime parsedAt;

    @PrePersist
    public void onPrePersist() {
        this.parsedAt = LocalDateTime.now();
    }
}