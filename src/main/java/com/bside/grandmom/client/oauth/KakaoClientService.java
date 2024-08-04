package com.bside.grandmom.client.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoClientService {
    @Value("${oauth.kakao.client_id}")
    String clientId;
    @Value("${oauth.kakao.redirect_uri}")
    String redirectUri;
    @Value("${oauth.kakao.admin_key}")
    String adminKey;

    private final RestTemplate restTemplate;
    HttpHeaders headers = new HttpHeaders();


    String AUTH_URL = "https://kauth.kakao.com";
    String API_URL = "https://kapi.kakao.com";

    public KakaoClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String  authorize() {
        String request = AUTH_URL + "/oauth/authorize?client_id=" + clientId
                + "&redirect_uri=" + redirectUri + "&response_type=code";
        return request;
    }

    public Map getProfile() {
        headers.set("Authorization", "KakaoAK " + adminKey);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        return null;
    }

    public Map getToken(String code) {
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("client_id", clientId);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForObject(AUTH_URL + "/oauth/token", requestEntity, Map.class);


    }
}
