package com.bside.grandmom.diaries.service;

import com.bside.grandmom.client.internalai.InternalAiClientService;
import com.bside.grandmom.client.internalai.dto.SummaryResponseModel;
import com.bside.grandmom.client.openai.OpenAiClientService;
import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.diaries.domain.DiaryDetailEntity;
import com.bside.grandmom.diaries.domain.DiaryInfoEntity;
import com.bside.grandmom.diaries.domain.QuestionEntity;
import com.bside.grandmom.diaries.dto.DiaryResDto;
import com.bside.grandmom.diaries.dto.QuestionReqDto;
import com.bside.grandmom.diaries.dto.QuestionResDto;
import com.bside.grandmom.diaries.dto.QuestionResDto.QuestionValue;
import com.bside.grandmom.diaries.dto.QuestionResDto.SummaryValue;
import com.bside.grandmom.diaries.prompt.Prompt;
import com.bside.grandmom.users.domain.UserEntity;
import com.bside.grandmom.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private static final String QUESTION = "QUESTION";
    private static final String SUMMARY = "SUMMARY";

    private static final int REQUIRE_ANSWER_COUNT_FOR_SUMMARY = 3;

    private static final Logger log = LoggerFactory.getLogger(DiaryService.class);

    private final OpenAiClientService openAiClientService;
    private final InternalAiClientService internalAiClientService;
    private final UserService userService;
    private final QuestionService questionService;

    private final DiaryInfoService diaryInfoService;
    private final DiaryDetailService diaryDetailService;

    @Value("${openai.vision-url}")
    private String visionUrl;

    /**
     * OpenAi 이용한 이미지 분석 (vision)
     */
    @Transactional
    public ResponseDto describeImage(MultipartFile image, long userNo) throws IOException {
        // header에서 did, uid 추출
//        AccessContext context = AccessContextHolder.getAccessContext();
        UserEntity user = userService.getUser(userNo);

        // (임시) user 테이블의 upd_dt를 기준으로 upd_dt가 오늘 날짜면 더이상 생성하지 못하도록 설정
        ResponseDto<Void> checkDiaryCnt = getCreateDiaryCnt(user);
        if (checkDiaryCnt != null) return checkDiaryCnt;


        // 이미지 분석 및 저장
        String prompt = Prompt.DESCRIBE.getPrompt();
        String base64Image = encodeImage(image);

        Map<String, Object> requestBody = openAiClientService.createVisionRequestBody(base64Image, prompt);
        Map<String, Object> describeText = openAiClientService.callOpenAiApi(visionUrl, requestBody);
        String imageDesc = handleVisionApiResponse(describeText);
        log.info("이미지 설명: {}", imageDesc);

        user.updateImgDesc(imageDesc, new Date());
        user.increaseUseCnt();
        // 첫번째 질문 생성 및 저장
        String response = internalAiClientService.createFirstInterview(imageDesc);
        questionService.createDiarySession(user, response);

        String imageInfo = settingImages(image, userNo);
        DiaryInfoEntity diaryInfo = DiaryInfoEntity.builder()
                .user(user)
                .img(imageInfo).build();
        diaryInfoService.saveImage(diaryInfo);

//      질의응답 관련 diary 테이블 값 생성 제거 (세션) 관련 로직 별도 서비스로 분리
//        diaryRepository.deleteByUser(user);
//        DiaryEntity diaryEntity = DiaryEntity.builder()
//                .question(response)
//                .user(user)
//                .answerCount(1)
//                .build();
//        diaryRepository.save(diaryEntity);

        Map<String, Object> result = new HashMap<>();
        result.put("question", response);
        return ResponseDto.success(result);
    }

    private String settingImages(MultipartFile image, Long userNo) throws IOException {
//        Map<String, String> result = new HashMap<>();
        String imagePath = "/root/grandmom/grandmom-image/";
//        String imagePath = "C:\\Users\\HPT\\grandMom\\grandmom-image\\";
        File imageDirectory = new File(imagePath);

        // 디렉토리가 없으면 생성
        if (!imageDirectory.exists()) {
            boolean dirCreated = imageDirectory.mkdirs();
            if (!dirCreated) {
                throw new IOException("Failed to create directory: " + imageDirectory.getAbsolutePath());
            }
        }
        String uuid = userNo + "_" + UUID.randomUUID();
        String imageName = uuid + "_" + image.getOriginalFilename();
        imageName = imageName.replaceAll("\\s", "");
        File saveFile = new File(imagePath, imageName);

        // 이미지 저장
        image.transferTo(saveFile);
        imagePath = imagePath + imageName;

//        result.put("imagePath", imagePath);
//        result.put("imageName", imageName);
        return imagePath;
    }

    private ResponseDto<Void> getCreateDiaryCnt(UserEntity user) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String todayStr = sdf.format(new Date());
//        if (user.getUpdDt() == null) return null;
//        String userUpdDtStr = sdf.format(user.getUpdDt());
//        try {
//            Date today = sdf.parse(todayStr);
//            Date userUpdDt = sdf.parse(userUpdDtStr);
//
//            if (userUpdDt.equals(today)) {
//                return ResponseDto.error("1", "과금 문제로 인해 일 1회로 횟수를 제한하고 있습니다. 이용해주셔서 감사합니다.");
//            }
//        } catch (ParseException e) {
//            return ResponseDto.error("999", "날짜 형식 변환 오류");
//        }
//        return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(new Date());
        if (user.getUpdDt() == null) return null;
        String userUpdDtStr = sdf.format(user.getUpdDt());
        int useCnt = user.getUseCnt();
        try {
            Date today = sdf.parse(todayStr);
            Date userUpdDt = sdf.parse(userUpdDtStr);
            if (userUpdDt.equals(today) && useCnt >= 3) {
                return ResponseDto.error("1", "과금 문제로 인해 일 3회로 횟수를 제한하고 있습니다. 이용해주셔서 감사합니다.");
            } else if (!userUpdDt.equals(today)) {
                user.resetUseCnt();
                return null;
            }
        } catch (ParseException e) {
            return ResponseDto.error("999", "날짜 형식 변환 오류");
        }
        return null;
    }

    /**
     * vision api response 값 extract
     */
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
     */
    private String encodeImage(MultipartFile imageFile) throws IOException {
        byte[] imageBytes = imageFile.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

//    public ResponseDto<QuestionResDto> question(QuestionReqDto request, long userNo) {
////        AccessContext context = AccessContextHolder.getAccessContext();
//        UserEntity user = userService.getUser(userNo);
//        questionService.addAnswer(user, request.getAnswerCount(), request.getAnswer());
//        log.info("대답:{}", request.getAnswer());
//
//        String operationType = determineOperationType(request);
//
//        List<QuestionEntity> chatHistories = questionService.getChatHistories(user);
//        QuestionResDto.Value value = callAPI(operationType, request, user, chatHistories);
//
//        if (operationType.equals(QUESTION)) {
//            questionService.addQuestion(user, request.getAnswerCount() + 1, ((QuestionValue) value).question());
//        }
//        QuestionResDto questionResDto = new QuestionResDto(operationType, value, request.getAnswerCount() + 1);
//        DiaryInfoEntity diaryInfo = diaryInfoService.getDiaryInfoEntity(userNo);
//
//        DiaryDetailEntity diaryDetail = DiaryDetailEntity.builder()
//                .diaryInfo(diaryInfo)
//                .userNo(userNo)
//                .v1("")
//                .v2("")
//                .v3("").build();
//        diaryDetailService.saveImage(diaryDetail);
//        return ResponseDto.success(questionResDto);
//    }

    public ResponseDto<QuestionResDto> question(QuestionReqDto request, long userNo) {
        // UserEntity 객체 가져오기
        UserEntity user = userService.getUser(userNo);
        // 사용자의 응답 저장
        questionService.addAnswer(user, request.getAnswerCount(), request.getAnswer());
        log.info("대답:{}", request.getAnswer());

        // operationType 결정
        String operationType = determineOperationType(request);

        // 기존 질문 히스토리 가져오기
        List<QuestionEntity> chatHistories = questionService.getChatHistories(user);
        // API 호출하여 새로운 질문 또는 요약 값 가져오기
        QuestionResDto.Value value = callAPI(operationType, request, user, chatHistories);

        // 새로운 질문이 생성된 경우 DB에 저장
        if (operationType.equals(QUESTION)) {
            questionService.addQuestion(user, request.getAnswerCount() + 1, ((QuestionValue) value).question());
        }

        // QuestionResDto 객체 생성
        QuestionResDto questionResDto = new QuestionResDto(operationType, value, request.getAnswerCount() + 1);

        // DiaryInfoEntity 가져오기
        DiaryInfoEntity diaryInfo = diaryInfoService.getLatestDiaryInfoByUserNo(userNo);

        // DiaryDetailEntity 설정 및 저장
        DiaryDetailEntity diaryDetail;
        if (operationType.equals(SUMMARY) && value instanceof SummaryValue summaryValue) {
            diaryDetail = DiaryDetailEntity.builder()
                    .diaryInfo(diaryInfo)
                    .userNo(userNo)
                    .v1(summaryValue.summary1())
                    .v2(summaryValue.summary2())
                    .v3(summaryValue.summary3()).build();
        } else {
            diaryDetail = DiaryDetailEntity.builder()
                    .diaryInfo(diaryInfo)
                    .userNo(userNo)
                    .v1("")
                    .v2("")
                    .v3("").build();
        }

        diaryDetailService.saveImage(diaryDetail);
        return ResponseDto.success(questionResDto);
    }

    private String determineOperationType(QuestionReqDto request) {
        if (request.getAnswerCount() >= REQUIRE_ANSWER_COUNT_FOR_SUMMARY) {
            return SUMMARY;
        } else {
            return QUESTION;
        }
    }

    private QuestionResDto.Value callAPI(String operationType, QuestionReqDto request, UserEntity user, List<QuestionEntity> chatHistories) {
        try {
            return switch (operationType) {
                case QUESTION -> {
                    String question = internalAiClientService.additionalInterview(user.getImgDesc(), chatHistories, request.getAnswer());
                    yield new QuestionValue(question);
                }
                case SUMMARY -> {
                    SummaryResponseModel summary = internalAiClientService.summary(user.getImgDesc(), chatHistories);
                    yield SummaryValue.from(summary.getDiaries());
                }
                default -> throw new IllegalArgumentException("지원하지 않는 operationType: " + operationType);
            };
        } catch (Exception e) {
            log.error("질문 요청 중 오류 발생", e);
            throw new IllegalStateException(e);
        }
    }

    private boolean checkUseCount() {
        return true;
    }

    public ResponseDto<List<DiaryResDto>> getDiaryList(long userNo){
        return ResponseDto.success(diaryDetailService.findAllWithImgByUserNo(userNo));
    }
}
