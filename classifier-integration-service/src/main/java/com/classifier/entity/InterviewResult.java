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

    // Store as a JSON object in MySQL
    @Column(columnDefinition = "json")
    private JsonNode overall;  // Using JsonNode from Jackson to map JSON data
}
