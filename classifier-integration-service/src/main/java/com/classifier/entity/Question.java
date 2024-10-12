package com.classifier.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Unique identifier for each record

    @Column(name = "question_id", length = 255)
    private String questionId; // ID for the question

    @Column(name = "question_text", columnDefinition = "TEXT")
    private String questionText; // The actual question text

    @Column(name = "difficulty")
    private int difficulty; // Difficulty level (1, 2, 3, etc.)

    @Column(name = "llm_difficulty")
    private int llmDifficulty; // LLM difficulty level

    @Column(name = "technology", length = 255)
    private String technology; // Technology type related to the question

    @Column(name = "llm_technology", length = 255)
    private String llmTechnology; // LLM technology type related to the question

    @Column(name = "interview_id", length = 255)
    private Long interviewId; // ID of the interview to which the question belongs

    @Column(name = "model_version", length = 50)
    private String modelVersion; // Version of the model used

    @Column(name = "created_ts", insertable = false, updatable = false)
    private OffsetDateTime createdTs;
    }