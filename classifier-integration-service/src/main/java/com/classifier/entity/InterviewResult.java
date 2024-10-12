package com.classifier.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "interview_id")
    private Long interviewId;

    private String name;

    @Column(name = "interview_complexity")
    private Integer interviewComplexity;

    private String experience;

    private String overall;
}
