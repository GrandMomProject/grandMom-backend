package com.bside.grandmom.diaries.service;

import com.bside.grandmom.diaries.prompt.Prompt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryService {
    @Value("${openai.api.key}")
    private String APIKEY;
    private final RestTemplate restTemplate;

    public ResponseEntity<String> describeImage(String imageUrl) {
        String prompt = Prompt.DESCRIBE.getPrompt();

        // 요청 본문 작성
        Map<String, Object> imageContent = new HashMap<>();
        imageContent.put("type", "image_url");
        Map<String, String> imageUrlContent = new HashMap<>();
        imageUrlContent.put("url", imageUrl);
        imageUrlContent.put("detail", "low");
        imageContent.put("image_url", imageUrlContent);

        Map<String, Object> textContent = new HashMap<>();
        textContent.put("type", "text");
        textContent.put("text", prompt);

        Map<String, Object>[] contents = new Map[]{textContent, imageContent};

        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", contents);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("messages", new Map[]{userMessage});
        requestBody.put("max_tokens", 300);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(APIKEY);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // OpenAI API 호출
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        try {
            Map<String, Object> response = restTemplate.postForObject(apiUrl, requestEntity, Map.class);

            // 응답 반환
            if (response != null && response.containsKey("choices")) {
                return ResponseEntity.ok(response.get("choices").toString());
            } else {
                return ResponseEntity.status(500).body("No response from OpenAI API");
            }
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error: " + e.getMessage());
        }
    }
}
