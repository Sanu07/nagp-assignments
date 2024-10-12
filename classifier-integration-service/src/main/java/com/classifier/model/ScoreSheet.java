package com.classifier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
public class ScoreSheet {
    private String technology;
    private Integer score;

    public ScoreSheet(String technology, Integer score) {
        this.technology = technology;
        this.score = score;
    }
}
