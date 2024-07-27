package com.bside.grandmom.diaries.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class InternalAiService {
    private final RestTemplate restTemplate;
    private final String host;

    public InternalAiService(RestTemplate restTemplate, @Value("${internal-ai.access-url}") String host) {
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
}
