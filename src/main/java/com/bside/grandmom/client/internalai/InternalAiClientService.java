package com.bside.grandmom.client.internalai;

import com.bside.grandmom.client.internalai.dto.ChatHistoryRequestModel;
import com.bside.grandmom.client.internalai.dto.SummaryResponseModel;
import com.bside.grandmom.diaries.domain.DiarySessionEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Service
@Slf4j
public class InternalAiClientService {
    private static final ObjectMapper MAPPER = new ObjectMapper();

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

    public String additionalInterview(String imageDescription, List<DiarySessionEntity> chatHistories, String answer) throws Exception {
        Map<String, String> requestBody = Map.ofEntries(
            entry("chatHistory", convertChatHistory(imageDescription, chatHistories)),
            entry("answer", answer)
        );

        ResponseEntity<String> response = restTemplate.postForEntity(host + "/additional-interview", requestBody, String.class);
        return response.getBody();
    }

    public SummaryResponseModel summary(String imageDescription, List<DiarySessionEntity> chatHistories) throws Exception {
        Map<String, String> requestBody = Map.of("chatHistory", convertChatHistory(imageDescription, chatHistories));

        ResponseEntity<String> response = restTemplate.postForEntity(host + "/summary", requestBody, String.class);
        return  MAPPER.readValue(response.getBody(), SummaryResponseModel.class);
    }

    private String convertChatHistory(String imageDescription, List<DiarySessionEntity> chatHistories) throws Exception {
        List<ChatHistoryRequestModel.ChatHistoryModel> chatHistoryModelModels = chatHistories.stream()
                .sorted(Comparator.comparing(DiarySessionEntity::getAnswerCount))
                .map(e -> new ChatHistoryRequestModel.ChatHistoryModel(e.getQuestion(), e.getAnswer()))
                .toList();

        ChatHistoryRequestModel requestModel = new ChatHistoryRequestModel(imageDescription, chatHistoryModelModels);
        return MAPPER.writeValueAsString(Map.of("chatHistory", requestModel));
    }

}
