package com.bside.grandmom.diaries.service;

import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.diaries.prompt.Prompt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final OpenAiClientService openAiClientService;
    private final InternalAiService internalAiService;

    @Value("${openai.vision-url}")
    private String VISIONURL;

    /**
     * OpenAi 이용한 이미지 분석 (vision)
     * */
    public ResponseDto describeImage(MultipartFile image) throws IOException {
        String prompt = Prompt.DESCRIBE.getPrompt();
        String base64Image = encodeImage(image);

        // requestBody 구성
        Map<String, Object> requestBody = openAiClientService.createVisionRequestBody(base64Image, prompt);

        Map<String, Object> describeText = openAiClientService.callOpenAiApi(VISIONURL, requestBody);
        String response = internalAiService.createFirstInterview(handleVisionApiResponse(describeText));

        Map<String, Object> result = new HashMap<>();
        result.put("question", response);
        return ResponseDto.success(result);
    }

    /**
     * vision api response 값 extract
     * */
    private String handleVisionApiResponse(Map<String, Object> response) {
        // 응답 반환
        if (response != null && response.containsKey("choices")) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> choices = objectMapper.convertValue(response.get("choices"), List.class);
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            return (String) message.get("content");
        } else {
            return null;
        }
    }

    /**
     * encode the image
     * */
    private String encodeImage(MultipartFile imageFile) throws IOException {
        byte[] imageBytes = imageFile.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

}
