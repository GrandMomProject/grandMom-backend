package com.bside.grandmom.diaries.service;

import com.bside.grandmom.client.internalai.InternalAiClientService;
import com.bside.grandmom.client.openai.OpenAiClientService;
import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.context.AccessContext;
import com.bside.grandmom.context.AccessContextHolder;
import com.bside.grandmom.diaries.dto.QuestionReqDto;
import com.bside.grandmom.diaries.prompt.Prompt;
import com.bside.grandmom.users.domain.UserEntity;
import com.bside.grandmom.users.service.UserService;
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
    private static final int REQUIRE_ANSWER_COUNT_FOR_SUMMARY = 3;

    private final OpenAiClientService openAiClientService;
    private final InternalAiClientService internalAiClientService;
    private final UserService userService;

    @Value("${openai.vision-url}")
    private String visionUrl;

    /**
     * OpenAi 이용한 이미지 분석 (vision)
     * */
    public ResponseDto<Map<String, Object>> describeImage(MultipartFile image) throws IOException {
        String prompt = Prompt.DESCRIBE.getPrompt();
        String base64Image = encodeImage(image);

        // requestBody 구성
        Map<String, Object> requestBody = openAiClientService.createVisionRequestBody(base64Image, prompt);

        Map<String, Object> describeText = openAiClientService.callOpenAiApi(visionUrl, requestBody);
        String response = internalAiClientService.createFirstInterview(handleVisionApiResponse(describeText));

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

    public ResponseDto question(QuestionReqDto request) {
        AccessContext context = AccessContextHolder.getAccessContext();
        if (!context.isValid()) {
            throw new IllegalStateException("uid or did 없음");
        }

        UserEntity user = userService.getUser(context.uid(), context.did());



        if (REQUIRE_ANSWER_COUNT_FOR_SUMMARY < request.getAnswerCount()) {
            internalAiClientService.additionalInterview(user.getImgDesc(), List.of(), request.getAnswer());
        } else {
//            internalAiClientService.summary();
        }

        return null;
    }
}
