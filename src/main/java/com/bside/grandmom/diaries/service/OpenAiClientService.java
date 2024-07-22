package com.bside.grandmom.diaries.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiClientService {
    @Value("${openai.api.key}")
    private String APIKEY;


    private final RestTemplate restTemplate;

    public OpenAiClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> callOpenAiApi(String visionUrl, Map<String, Object> requestBody) {
        // header 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(APIKEY);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForObject(visionUrl, requestEntity, Map.class);
    }

    public Map<String, Object> createVisionRequestBody(String base64Image, String prompt) {
        // 요청 본문 작성
        Map<String, Object> imageContent = new HashMap<>();
        imageContent.put("type", "image_url");
        Map<String, String> imageUrlContent = new HashMap<>();
        imageUrlContent.put("url", "data:image/jpeg;base64," + base64Image);
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

        return requestBody;
    }
}
