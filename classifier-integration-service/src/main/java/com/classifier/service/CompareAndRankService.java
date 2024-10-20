package com.classifier.service;

import com.classifier.constants.Constants;
import com.classifier.dao.InterviewResultRepository;
import com.classifier.entity.InterviewResult;
import com.classifier.model.AIResponse;
import com.classifier.model.CompareAndRankPromptInput;
import com.classifier.model.ComparedResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompareAndRankService {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private InterviewResultRepository interviewResultRepository;

    @Autowired
    private ObjectMapper mapper;

    public ComparedResult compareAndRank(String jobDescription, List<String> candidateNames) {
        List<InterviewResult> interviewResults = interviewResultRepository.findByNameInIgnoreCase(candidateNames);
        String prompt = String.format(Constants.PROMPT_COMPARE_AND_RANK_CANDIDATES, jobDescription);
        List<CompareAndRankPromptInput> compareAndRankPromptInputs = interviewResults.stream().map(interviewResult -> {
            JsonNode jsonNode = mapper.valueToTree(interviewResult.getOverall());
            return CompareAndRankPromptInput.builder()
                    .name(interviewResult.getName())
                    .interviewComplexity(interviewResult.getInterviewComplexity())
                    .experience(interviewResult.getExperience())
                    .overall(jsonNode)
                    .build();
        }).toList();
        Map<String, Map<String, Object>> nameOverallMap = compareAndRankPromptInputs.stream()
                .collect(Collectors.toMap(
                        CompareAndRankPromptInput::getName,
                        e -> {
                            try {
                                Map<String, Object> map = mapper.readValue(e.getOverall().textValue(), Map.class);
                                map.put("experience", e.getExperience());
                                return map;
                            } catch (JsonProcessingException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                ));
        String input;
        try {
            input = mapper.writeValueAsString(compareAndRankPromptInputs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        AIResponse aiResponse = geminiService.getAIResponse(prompt, input);
        String text = aiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
        text = text.replaceAll("```json", "").replace("```", "");
        try {
            ComparedResult comparedResult = mapper.readValue(text, ComparedResult.class);
            comparedResult.getResults().forEach(result -> {
                result.setOverall(nameOverallMap.get(result.getName()));
            });
            return comparedResult;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
