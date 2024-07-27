package com.bside.grandmom.client.internalai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Service
@Slf4j
public class InternalAiClientService {
    private final RestTemplate restTemplate;
    private final String host;

    public InternalAiClientService(RestTemplate restTemplate, @Value("${internal-ai.access-url}") String host) {
        this.restTemplate = restTemplate;
        this.host = host;
    }

    public String createFirstInterview(String describeImage) {
        Map<String, Object> requestBody = new HashMap<>();
        log.debug("describe image: {}", describeImage);
        requestBody.put("imageSummary", describeImage);
        log.debug("requestBody: {}", requestBody);
        ResponseEntity<String> response = restTemplate.postForEntity(host + "/first-interview", requestBody, String.class);
        log.debug("response: {}", response);
        return response.getBody();
    }

    private static final String ADDITIONAL_INTERVIEW_FORMAT = """
            
            """;

    public String additionalInterview(String imgDesc, List<String> chatHistories, String answer) {
        Map<String, String> requestBody = Map.ofEntries(
            entry("chatHistory", chatHistories.toString()),
            entry("answer", answer)
        );
        ResponseEntity<String> response = restTemplate.postForEntity(host + "/additional-interview", requestBody, String.class);
        return response.getBody();
    }

    public List<String> summary(String chatHistory) {
//        Map<String, String> requestBody = Map.of("chatHistory", chatHistory);
//        ResponseEntity<List<String>> response = restTemplate.postForEntity(host + "/summary", requestBody, List.class);
//        return response.getBody();
        return null;
    }

}
