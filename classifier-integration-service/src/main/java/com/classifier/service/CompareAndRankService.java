package com.classifier.service;

import com.classifier.constants.Constants;
import com.classifier.dao.InterviewResultRepository;
import com.classifier.entity.InterviewResult;
import com.classifier.model.AIResponse;
import com.classifier.model.ComparedResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        String input;
        try {
            input = mapper.writeValueAsString(interviewResults);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        AIResponse aiResponse = geminiService.getAIResponse(prompt, input);
        String text = aiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
        try {
            ComparedResult comparedResult = mapper.readValue(text, ComparedResult.class);
            return comparedResult;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
