package com.classifier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AnalyseResponse {

    private Analysis analysis;
    private List<Evaluation> evaluation;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class Analysis {
        private Integer interviewComplexity;
        private Map<String, Integer> overall;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class Evaluation {
        private Integer marksObtained;
        private List<String> feedback;
    }
}
