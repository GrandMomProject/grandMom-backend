package com.bside.grandmom.diaries.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NcpClientService {
    @Value("${ncp.common-url}")
    private String NCPURL;

    private final RestTemplate restTemplate;
    HttpHeaders headers = new HttpHeaders();

    public NcpClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createFirstInterview(String describeImage) {
        Map<String, Object> requestBody = new HashMap<String, Object>();
        System.out.println("describe image: " + describeImage);
        requestBody.put("imageSummary", describeImage);
        System.out.println("requestBody: " + requestBody);
        ResponseEntity<String> response = restTemplate.postForEntity(NCPURL+"/first-interview", requestBody, String.class);
        System.out.println("response: " + response);
        return response.getBody();



    }
}
