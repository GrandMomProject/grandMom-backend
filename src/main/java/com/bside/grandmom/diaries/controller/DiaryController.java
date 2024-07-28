package com.bside.grandmom.diaries.controller;

import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.diaries.dto.ImageReqDto;
import com.bside.grandmom.diaries.dto.QuestionReqDto;
import com.bside.grandmom.diaries.dto.QuestionResDto;
import com.bside.grandmom.diaries.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.Map;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
@Tag(name = "DIARY API", description = "일기 API")
@Slf4j
public class DiaryController {
    private final DiaryService diaryService;
    private final int RESTRICTCNT = 100;
    private int callCnt = 0;

    @Operation(summary = "이미지 전송 API", description = "사진을 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 전송 성공"),
            @ApiResponse(responseCode = "400", description = "에러 코드 정의 필요")
    })
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> image(
            @RequestPart("image") MultipartFile image,
            @RequestPart(name = "req", required = false) ImageReqDto req) throws IOException {
        if (callCnt >= RESTRICTCNT) {
            return ResponseEntity.ok(ResponseDto.error("1", "과금 문제로 인해 일 이용 횟수를 제한하고 있습니다. 이용해주셔서 감사합니다."));
        }
        callCnt++;
        return ResponseEntity.ok(diaryService.describeImage(image));
    }

    @Operation(
            summary = "질문 요청 API", description = "대답을 보내고 질문을 받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 전송 성공"),
            @ApiResponse(responseCode = "400", description = "에러 코드 정의 필요")})
    @PostMapping("/question")
    public ResponseEntity<ResponseDto<QuestionResDto>> question(@RequestBody QuestionReqDto req) {
        return ResponseEntity.ok(diaryService.question(req));
    }

}
