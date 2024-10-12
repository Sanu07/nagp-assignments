package com.classifier.service;

import com.classifier.constants.Constants;
import com.classifier.dao.InterviewRepository;
import com.classifier.dao.InterviewResultRepository;
import com.classifier.entity.Interview;
import com.classifier.entity.InterviewResult;
import com.classifier.model.AIResponse;
import com.classifier.model.AnalyseRequest;
import com.classifier.model.AnalyseResponse;
import com.classifier.model.GeminiAnalyseRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class QnAAnalysisService {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InterviewResultRepository interviewResultRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    public AnalyseResponse getAnalyseResponse(AnalyseRequest analyseRequest) {
        GeminiAnalyseRequest geminiAnalyseRequest = new GeminiAnalyseRequest();
        geminiAnalyseRequest.setExperience(geminiAnalyseRequest.getExperience());
        geminiAnalyseRequest.setQuestionsAndAnswers(analyseRequest.getQuestionAndAnswers()
                .stream()
                .map(request -> GeminiAnalyseRequest.QuestionsAndAnswers.builder()
                        .question(request.getQuestion())
                        .answer(request.getAnswer())
                        .technology(request.getSelectedTechnology())
                        .difficulty(request.getSelectedDifficulty())
                        .build()).collect(Collectors.toList()));
        try {
            String input = mapper.writeValueAsString(geminiAnalyseRequest);
            AIResponse aiResponse = geminiService.getAIResponse(Constants.PROMPT_INDIVIDUAL_REPORT_ANALYSIS, input);
            String text = aiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
            AnalyseResponse analyseResponse = mapper.readValue(text, AnalyseResponse.class);

            Interview interview = interviewRepository.findById(analyseRequest.getInterviewId()).get();

            interviewResultRepository.save(InterviewResult.builder()
                    .interviewId(analyseRequest.getInterviewId())
                    .experience(geminiAnalyseRequest.getExperience())
                    .name(interview.getInterviewee())
                    .interviewComplexity(analyseResponse.getAnalysis().getInterviewComplexity())
                    .overall(mapper.valueToTree(analyseResponse.getAnalysis().getOverall()))
                    .build());
            return analyseResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
