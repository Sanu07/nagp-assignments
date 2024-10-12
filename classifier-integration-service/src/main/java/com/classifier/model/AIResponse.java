package com.classifier.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AIResponse {

    private List<Candidates> candidates;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidates {
        private Content content;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private List<Part> parts;
        private String role;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {
        private String text;
    }
}
