package com.classifier.service;

import com.classifier.constants.Constants;
import com.classifier.dao.InterviewRepository;
import com.classifier.dao.InterviewResultRepository;
import com.classifier.dao.ScoreRepository;
import com.classifier.entity.Interview;
import com.classifier.entity.InterviewResult;
import com.classifier.entity.Score;
import com.classifier.model.AIResponse;
import com.classifier.model.InterviewDetails;
import com.classifier.model.ScoreSheet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TextSanitizationService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    public InterviewDetails getSanitizedText(MultipartFile docFile, MultipartFile scoreFile, Integer experience, String genus) throws IOException {

        Interview savedInterview = interviewRepository.save(Interview.builder()
                .yearsOfExperience(experience)
                .genus(genus)
                .createsTs(OffsetDateTime.now())
                .build());

        // get the interviewer scores from csv file
        List<ScoreSheet> scores = getScores(scoreFile);
        // TODO save the score in database
        // save in database
        scoreRepository.save(Score.builder()
                .interviewId(savedInterview.getId())
                .score(mapper.writeValueAsString(scores)).build());

        // get the conversation Text
        // Sanu Ghosh: what is java class?
        String formattedText = getConversationText(docFile);

        // call the PROMPT to get the ques and ans and interviewer/interviewee name
        AIResponse interviewDetails = geminiService.getAIResponse(Constants.PROMPT_SANITIZE_TEXT, formattedText);

        //call the predict endpoint in LLM to get difficulty, technology and suggested questions
        InterviewDetails questionTags = getQuestionTags(interviewDetails);

        savedInterview.setInterviewer(questionTags.getInterviewer());
        savedInterview.setInterviewee(questionTags.getInterviewee());
        interviewRepository.save(savedInterview);
        return questionTags;
    }

    private InterviewDetails getQuestionTags(AIResponse aiResponse) {
        String interviewDetailsPromptResponse = aiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
        InterviewDetails interviewDetails = null;
        try {
            interviewDetails = mapper.readValue(interviewDetailsPromptResponse, InterviewDetails.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<InterviewDetails.QnA> llmResponse = getLLMResponse(interviewDetails);
        InterviewDetails interviewDetailsResponse = getInterviewDetails(interviewDetails, llmResponse);
        return interviewDetailsResponse;
    }

    private InterviewDetails getInterviewDetails(InterviewDetails interviewDetails, List<InterviewDetails.QnA> llmResponse) {
        InterviewDetails interviewDetailsResponse = new InterviewDetails();
        interviewDetailsResponse.setInterviewer(interviewDetails.getInterviewer());
        interviewDetailsResponse.setInterviewee(interviewDetails.getInterviewee());
        List<InterviewDetails.QnA> qnAList = new ArrayList<>();
        for (int i = 0; i < llmResponse.size(); i++) {
            InterviewDetails.QnA qnA = llmResponse.get(i);
            qnA.setAnswer(interviewDetails.getConversations().get(i).getAnswer());
            qnAList.add(qnA);
        }
        interviewDetailsResponse.setConversations(qnAList);
        return interviewDetailsResponse;
    }

    private String getConversationText(MultipartFile docFile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("docFile", new org.springframework.core.io.ByteArrayResource(docFile.getBytes()) {
            @Override
            public String getFilename() {
                return docFile.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String url = "http://localhost:5000/extract_text";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return formatConversations(response.getBody());
    }

    private List<ScoreSheet> getScores(MultipartFile file) {
        List<ScoreSheet> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            // Read each line from the CSV
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(","); // Assuming CSV is comma-separated
                if (columns.length >= 2) { // Check if there are at least two columns
                    String technology = columns[0].trim(); // First column
                    Integer score = Integer.valueOf(columns[1].trim()); // Second column
                    scores.add(new ScoreSheet(technology, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scores;
    }

    private String formatConversations(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String jsonConversationString = jsonObject.getString("conversation");

        // Initialize StringBuilder to construct the final text
        StringBuilder result = new StringBuilder();

        // Parse the JSON array
        JSONArray jsonArray = new JSONArray(jsonConversationString);

        // Iterate through each JSON object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String speaker = obj.getString("Speaker");
            String conversation = obj.getString("Conversation");

            // Append formatted text
            result.append(speaker).append(": ").append(conversation).append("\n\n"); // Adding two newlines for a gap
        }

        return result.toString().trim(); // Remove trailing whitespace/newlines
    }

    private List<InterviewDetails.QnA> getLLMResponse(InterviewDetails interviewDetails) {
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        List<InterviewDetails.QnA> questionList = interviewDetails.getConversations().stream()
                .peek(detail -> detail.setId(UUID.randomUUID().toString())).collect(Collectors.toList());

        // Create HTTP request with the list of questions as the body
        HttpEntity<List<InterviewDetails.QnA>> request = new HttpEntity<>(questionList, headers);

        // Make the POST request to the /predict endpoint
        ResponseEntity<List<InterviewDetails.QnA>> response = restTemplate.exchange(
                "http://localhost:5000/predict",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        // Return the list of InterviewDetails.QnA from the response body
        return response.getBody();
    }
}
