package com.classifier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ComparedResult {

    private JobAnalysis jobAnalysis;
    private List<Result> results;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class JobAnalysis {
        private String experience;
        private List<String> mandatorySkills;
        private List<String> goodToHave;
        private Map<String, Integer> weightage;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Result {
        private String name;
        private Integer rank;
        private List<String> feedbacks;
    }
}
