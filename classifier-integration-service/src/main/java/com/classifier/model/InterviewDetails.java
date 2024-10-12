package com.classifier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewDetails {
    private String interviewer;
    private String interviewee;
    private List<QnA> conversations;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QnA {
        private String id;
        private String question;
        private String answer;
        private Integer difficulty;
        private String technology;
        private List<String> suggestedQuestions;
    }
}
