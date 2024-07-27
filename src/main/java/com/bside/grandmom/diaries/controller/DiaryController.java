package com.bside.grandmom.diaries.controller;

import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.diaries.dto.*;
import com.bside.grandmom.diaries.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
@Tag(name = "DIARY API", description = "일기 API")
@Slf4j
public class DiaryController {
    private final DiaryService diaryService;

    @Operation(summary = "이미지 전송 API", description = "사진을 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 전송 성공"),
            @ApiResponse(responseCode = "400", description = "에러 코드 정의 필요")
    })
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> image(
            @RequestPart("image") MultipartFile image,
            @RequestPart(name = "req", required = false) ImageReqDto req) throws IOException {

        //            Thread.sleep(3000); // 3000 milliseconds = 3 seconds
        return diaryService.describeImage(image);
        /*ImageResDto res = new ImageResDto();
        res.setDiaryID("sampleID1234");
        res.setQuestion("이 사진은 어느 해변에서 찍으셨나요? 그리고 그곳에서 어떤 활동을 하셨는지 궁금합니다.");
        return ResponseDto.success(res);*/
    }

    @Operation(
            summary = "질문 요청 API", description = "대답을 보내고 질문을 받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 전송 성공"),
            @ApiResponse(responseCode = "400", description = "에러 코드 정의 필요")})
    @PostMapping("/question")
    public ResponseEntity<ResponseDto> question(@RequestBody QuestionReqDto req) throws IOException {
        int ansCnt = req.getAnswerCount() + 1;
        QuestionResDto resDto = new QuestionResDto();

        /*resDto.setAnswerCount(ansCnt);
        resDto.setType("QUESTION");
        QuestionResDto.QuestionValue value = new QuestionResDto.QuestionValue();
        value.setQuestion("날씨는 어땠나요?");
        resDto.setValue(value);*/

        QuestionResDto.SummaryValue value = new QuestionResDto.SummaryValue();
        value.setSummary1("일기 버전1");
        value.setSummary2("일기 버전2");
        value.setSummary3("일기 버전3");
        resDto.setValue(value);
        resDto.setAnswerCount(1);
        resDto.setType("SUMMARY");
        return ResponseDto.success(resDto);
    }





    @Operation(
            summary = "일기 생성 API", description = "업로드한 사진에 대한 주고받은 대화를 통해 일기를 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 생성 성공"),
            @ApiResponse(responseCode = "400", description = "에러 코드 정의 필요")})
    @GetMapping("/diary")
    public ResponseEntity<ResponseDto> diary(@RequestParam String diaryID) throws IOException {
        DiaryResDto res = new DiaryResDto();
        res.setDiaryID("sampleID1234");
        res.setSummary1("요약1번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약1번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약1번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약1번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약1번입니다 글자수를 채우기 위한 텍스트입니다.200글자");
        res.setSummary2("요약2번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약2번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약2번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약2번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약2번입니다 글자수를 채우기 위한 텍스트입니다.200글자");
        res.setSummary3("요약3번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약3번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약3번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약3번입니다 글자수를 채우기 위한 텍스트입니다.더미더미 더미더미 당근당근 요약3번입니다 글자수를 채우기 위한 텍스트입니다.200글자");
        res.setVoiceURL1("dummyURL1");
        res.setVoiceURL2("dummyURL2");
        res.setVoiceURL3("dummyURL3");
        return ResponseDto.success(res);
    }
}
