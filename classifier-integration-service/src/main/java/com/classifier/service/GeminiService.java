package com.classifier.service;

import com.classifier.model.AIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Objects;

@Service
public class GeminiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String geminiKey;

    private final String API_URL_TEMPLATE = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=%s";

    public AIResponse getAIResponse(String prompt, String input) {
        String apiUrl = String.format(API_URL_TEMPLATE, geminiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode contentNode = objectMapper.createObjectNode();
        ObjectNode contentPartsNode = objectMapper.createObjectNode();

        ObjectNode systemInstructionNode = objectMapper.createObjectNode();
        ObjectNode systemInstructionPartsNode = objectMapper.createObjectNode();

        systemInstructionPartsNode.put("text", prompt);
        systemInstructionNode.set("parts", objectMapper.createArrayNode().add(systemInstructionPartsNode));
        systemInstructionNode.put("role", "user");

        contentPartsNode.put("text", input);
        contentNode.set("parts", objectMapper.createArrayNode().add(contentPartsNode));
        contentNode.put("role", "user");

        ObjectNode requestBodyNode = objectMapper.createObjectNode();
        requestBodyNode.set("contents", objectMapper.createArrayNode().add(contentNode));
        requestBodyNode.set("systemInstruction", systemInstructionNode);

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestBodyNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to construct JSON request body", e);
        }

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<AIResponse> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, AIResponse.class);

        AIResponse AIResponse = response.getBody();
        if (Objects.nonNull(AIResponse)) {
            String text = AIResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
            text = text.replace("\n", "").replace("\t", "").trim();
            AIResponse.getCandidates().get(0).getContent().getParts().get(0).setText(text);
        }

        return response.getBody();
    }
}