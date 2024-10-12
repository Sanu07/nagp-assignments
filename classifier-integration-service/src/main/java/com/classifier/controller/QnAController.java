package com.classifier.controller;

import com.classifier.constants.Constants;
import com.classifier.model.*;
import com.classifier.service.CompareAndRankService;
import com.classifier.service.GeminiService;
import com.classifier.service.QnAAnalysisService;
import com.classifier.service.TextSanitizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("interview")
public class QnAController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private TextSanitizationService textSanitizationService;

    @Autowired
    private QnAAnalysisService qnAAnalysisService;

    @Autowired
    private CompareAndRankService compareAndRankService;

    @PostMapping("qna")
    public ResponseEntity<InterviewDetails> extractInterviewDetails(@RequestParam("docFile") MultipartFile docFile,
                                                                    @RequestParam("excelFile") MultipartFile excelFile,
                                                                    @RequestParam("experience") Integer experience,
                                                                    @RequestParam("genus") String genus) {


//        InterviewDetails interview1 = new InterviewDetails();
//        interview1.setInterviewer("John Doe");
//        interview1.setInterviewee("Alice Smith");
//
//        List<InterviewDetails.QnA> qnaList1 = new ArrayList<>();
//        InterviewDetails.QnA qna1 = new InterviewDetails.QnA();
//        qna1.setId("1");
//        qna1.setQuestion("What is polymorphism?");
//        qna1.setAnswer("Polymorphism allows objects to be treated as instances of their parent class.");
//        qna1.setDifficulty(3);
//        qna1.setTechnology("java");
//        qna1.setSuggestedQuestions(List.of("suggestion-1", "suggestion-2"));
//        qnaList1.add(qna1);
//
//        InterviewDetails.QnA qna2 = new InterviewDetails.QnA();
//        qna2.setId("2");
//        qna2.setQuestion("Explain Java 8 Streams.");
//        qna2.setAnswer("Streams provide a functional approach to processing collections of objects.");
//        qna2.setDifficulty(4);
//        qna2.setTechnology("devops");
//        qna2.setSuggestedQuestions(List.of("suggestion-1", "suggestion-2"));
//        qnaList1.add(qna2);
//
//        interview1.setQuestionsAndAnswers(qnaList1);
//        return ResponseEntity.ok(interview1);
        try {
            InterviewDetails sanitizedText = textSanitizationService.getSanitizedText(docFile, excelFile, experience, genus);
            return ResponseEntity.ok(sanitizedText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new InterviewDetails());
    }

    @PostMapping("updateDetails")
    public ResponseEntity<AnalyseResponse> updateDetails(@RequestBody AnalyseRequest analyseRequest) {
//        AnalyseResponse.Analysis analysis = new AnalyseResponse.Analysis();
//        analysis.setInterviewComplexity(3); // Dummy complexity value
//        Map<String, Integer> overallMap = new HashMap<>();
//        overallMap.put("Technical Skills", 4);
//        overallMap.put("Communication Skills", 5);
//        analysis.setOverall(overallMap);
//
//        // Create a dummy Evaluation object
//        AnalyseResponse.Evaluation evaluation = new AnalyseResponse.Evaluation();
//        evaluation.setMarksObtained(85); // Dummy marks
//        List<String> feedbackList = Arrays.asList("Excellent performance", "Good communication skills");
//        evaluation.setFeedback(feedbackList);
//
//        // Create a dummy AnalyseResponse object
//        AnalyseResponse response = new AnalyseResponse();
//        response.setAnalysis(analysis);
//        response.setEvaluation(evaluation);
//        return ResponseEntity.ok(response);
        AnalyseResponse analyseResponse = qnAAnalysisService.getAnalyseResponse(analyseRequest);
        return ResponseEntity.ok(analyseResponse);
    }

    @PostMapping("/compareAndRank")
    public ResponseEntity<ComparedResult> compareAndRankCandidates(
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam("candidates") String candidatesJson
    ) {
        try {
            List<String> candidates = new ObjectMapper().readValue(candidatesJson, new TypeReference<>() {
            });
            ComparedResult comparedResult = compareAndRankService.compareAndRank(jobDescription, candidates);
            return ResponseEntity.ok(comparedResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        Map<String, Integer> weightage = new HashMap<>();
//        weightage.put("Java", 5);
//        weightage.put("Spring Boot", 4);
//        weightage.put("Microservices", 4);
//
//        ComparedResult.JobAnalysis jobAnalysis = ComparedResult.JobAnalysis.builder()
//                .experience("5-7 years")
//                .mandatorySkills(Arrays.asList("Java", "Spring Boot", "Microservices"))
//                .goodToHave(Arrays.asList("DevOps", "AWS"))
//                .weightage(weightage)
//                .build();
//
//        // Result 1
//        ComparedResult.Result result1 = ComparedResult.Result.builder()
//                .name("Candidate 1")
//                .rank(1)
//                .feedbacks(Arrays.asList("Excellent Java skills", "Good knowledge of Spring Boot"))
//                .build();
//
//        // Result 2
//        ComparedResult.Result result2 = ComparedResult.Result.builder()
//                .name("Candidate 2")
//                .rank(2)
//                .feedbacks(Arrays.asList("Good experience with microservices", "Average knowledge of DevOps"))
//                .build();
//
//        ComparedResult.Result result3 = ComparedResult.Result.builder()
//                .name("Candidate 3")
//                .rank(3)
//                .feedbacks(Arrays.asList("Excellent Java skills", "Good knowledge of Spring Boot"))
//                .build();
//
//        // Build ComparedResult
//        ComparedResult comparedResult = ComparedResult.builder()
//                .jobAnalysis(jobAnalysis)
//                .results(Arrays.asList(result1, result2, result3))
//                .build();
//
//        // Handle the file and candidates list as required
//        return ResponseEntity.ok(comparedResult);

    }
}
