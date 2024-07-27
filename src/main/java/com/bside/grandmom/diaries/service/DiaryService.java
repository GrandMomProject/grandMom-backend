package com.bside.grandmom.diaries.service;

import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.diaries.prompt.Prompt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final OpenAiClientService openAiClientService;
    private final NcpClientService ncpClientService;

    @Value("${openai.vision-url}")
    private String VISIONURL;

    /**
     * OpenAi 이용한 이미지 분석 (vision)
     * */
    public ResponseEntity<ResponseDto> describeImage(MultipartFile image) throws IOException {
        String prompt = Prompt.DESCRIBE.getPrompt();
        String base64Image = encodeImage(image);

        // requestBody 구성
        Map<String, Object> requestBody = openAiClientService.createVisionRequestBody(base64Image, prompt);

        try {
            Map<String, Object> describeText = openAiClientService.callOpenAiApi(VISIONURL, requestBody);
            String response = ncpClientService.createFirstInterview(handleVisionApiResponse(describeText));
//            String response = ncpClientService.createFirstInterview("사진에는 두 사람이 공중에서 점프하고 있는 모습이 담겨 있습니다. 배경에는 아름다운 호숫가 풍경과 산들이 보이며, 주위에는 노란 잎사귀들이 떨어져 있습니다. 하늘은 푸르고 맑아 분위기가 화창합니다. 두 사람은 밝은 표정을 지으며 즐거운 순간을 공유하고 있는 것 같습니다.");

            return ResponseDto.success(response);
        } catch (Error e) {
            throw e;
        }
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
            String content = (String) message.get("content");
            return content;
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
