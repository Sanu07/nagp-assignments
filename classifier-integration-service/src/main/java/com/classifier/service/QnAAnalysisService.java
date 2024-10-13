package com.classifier.service;

import com.classifier.constants.Constants;
import com.classifier.dao.InterviewRepository;
import com.classifier.dao.InterviewResultRepository;
import com.classifier.dao.QuestionRepository;
import com.classifier.dao.ScoreRepository;
import com.classifier.entity.Interview;
import com.classifier.entity.InterviewResult;
import com.classifier.entity.Question;
import com.classifier.entity.Score;
import com.classifier.model.AIResponse;
import com.classifier.model.AnalyseRequest;
import com.classifier.model.AnalyseResponse;
import com.classifier.model.GeminiAnalyseRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    public AnalyseResponse getAnalyseResponse(AnalyseRequest analyseRequest) {
        GeminiAnalyseRequest geminiAnalyseRequest = new GeminiAnalyseRequest();
        geminiAnalyseRequest.setExperience(String.valueOf(analyseRequest.getExperience()));
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
            text = text.replaceAll("```json", "").replace("```", "");
            AnalyseResponse analyseResponse = mapper.readValue(text, AnalyseResponse.class);

            Optional<Interview> interview = Optional.ofNullable(interviewRepository.findById(analyseRequest.getInterviewId())
                    .orElse((Interview.builder()
                            .id(1L)
                            .interviewee("Interviewee-A")
                            .build())));

            interviewResultRepository.save(InterviewResult.builder()
                    .interviewId(analyseRequest.getInterviewId())
                    .experience(String.valueOf(analyseRequest.getExperience()))
                    .name(interview.get().getInterviewee())
                    .interviewComplexity(analyseResponse.getAnalysis().getInterviewComplexity())
                    .overall(mapper.writeValueAsString(analyseResponse.getAnalysis().getOverall()))
                    .build());

            List<Question> questionList = analyseRequest.getQuestionAndAnswers().stream()
                    .map(request -> Question.builder()
                            .createdTs(OffsetDateTime.now())
                            .llmTechnology(request.getLlmTechnology())
                            .llmDifficulty(request.getLlmDifficulty())
                            .technology(request.getSelectedTechnology())
                            .difficulty(request.getSelectedDifficulty())
                            .questionText(request.getQuestion())
                            .modelVersion("v1")
                            .interviewId(interview.get().getId())
                            .build()).collect(Collectors.toList());

            questionRepository.saveAll(questionList);
            return analyseResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Score getInterviewerScores(Long interviewId) {
        return scoreRepository.findByInterviewId(interviewId);
    }
}
