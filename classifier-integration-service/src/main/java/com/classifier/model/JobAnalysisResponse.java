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
public class JobAnalysisResponse {

    private JobAnalysis jobAnalysis;
    private List<Result> results;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JobAnalysis {
        private String experience;
        private String jobDescription;
        private List<String> mandatorySkills;
        private List<String> goodToHave;
        private Weightage weightage;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Weightage {
            private Integer microservice;
            private Integer devops;
            private Integer springboot;
            private Integer experience;
            private Integer java;
            private Integer database;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Result {
        private String name;
        private Integer rank;
        private Weightage weightage;
        private List<String> feedback;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Weightage {
            private String calculation;
            private List<String> explanation;
        }
    }
}
