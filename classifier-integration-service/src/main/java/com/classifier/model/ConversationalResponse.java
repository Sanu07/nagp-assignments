package com.classifier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ConversationalResponse {

    private String conversations;
    @JsonProperty("transcript_date")
    private String transcriptionDate;
}
