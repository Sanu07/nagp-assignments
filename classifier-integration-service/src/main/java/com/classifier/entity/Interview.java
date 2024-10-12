package com.classifier.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String interviewer;
    private String interviewee;
    private String genus;
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    @Column(name = "created_ts")
    private OffsetDateTime createsTs;
}
