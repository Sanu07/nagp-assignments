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
public class AnalyseRequest {

    private Long interviewId;
    private Integer experience;
    private List<QuestionAndAnswers> questionAndAnswers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionAndAnswers {
        private Integer questionId;
        private String question;
        private String answer;
        private String selectedTechnology;
        private Integer selectedDifficulty;
        private String llmTechnology;
        private Integer llmDifficulty;
    }
}
