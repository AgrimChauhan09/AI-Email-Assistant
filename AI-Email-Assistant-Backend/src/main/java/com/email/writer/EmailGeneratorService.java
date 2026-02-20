package com.email.writer;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class EmailGeneratorService {
    private final WebClient webClient;
    private final String apiKey;


    public EmailGeneratorService(WebClient.Builder webclientBuilder,
                                 @Value("${gemini.api.url}") String baseUrl,
                                 @Value("${gemini.api.key}") String geminiApiKey) {
        this.apiKey = geminiApiKey;
        this.webClient = webclientBuilder.baseUrl(baseUrl).build();
    }

    public String generateEmailReply(EmailRequest emailRequest) {
        // build prompt
        String prompt=buildPrompt(emailRequest);
        // prepare raw JSON body
        String requestBody=String.format("""
               {
                    "contents": [
                      {
                        "parts": [
                          {
                            "text": "%s"
                          }
                        ]
                      }
                    ]
                  }""",prompt);
        // send request

        String response=webClient.post()
                .uri(uriBuilder->uriBuilder
                                .path("/v1beta/models/gemini-3-flash-preview:generateContent")
                                .build())
                .header("x-goog-api-key",apiKey)
                .header("content-type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // extract response
        return  extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try{
            ObjectMapper mapper=new ObjectMapper();
            JsonNode root=mapper.readTree(response);
             return  root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an AI email assistant. Generate a SINGLE, ready-to-send email reply for the provided email.\n");
        prompt.append("Follow these STRICT RULES:\n");
        prompt.append("1. Output ONLY the exact email body. Do NOT include conversational filler like 'Here is your reply:' or 'Option 1'.\n");
        prompt.append("2. Do NOT provide multiple options. Give exactly one best response.\n");
        prompt.append("3. Do NOT include a Subject line.\n");
        prompt.append("4. Do NOT add any tips, explanations, or notes at the end.\n");

        // Tone check
        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append("5. Keep the tone ").append(emailRequest.getTone()).append(".\n\n");
        } else {
            prompt.append("5. Keep the tone professional.\n\n"); // Default tone
        }
        // Append original email once
        prompt.append("Original Email to reply to:\n");
        prompt.append(emailRequest.getEmailContent());

        return prompt.toString();
    }
}