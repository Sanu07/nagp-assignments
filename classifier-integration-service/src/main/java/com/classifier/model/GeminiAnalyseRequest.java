package com.classifier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiAnalyseRequest {

    private String experience;
    private List<QuestionsAndAnswers> questionsAndAnswers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionsAndAnswers {
        private String question;
        private String answer;
        private String technology;
        private Integer difficulty;
    }
}
