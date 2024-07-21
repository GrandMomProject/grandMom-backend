package com.bside.grandmom.diaries.controller;

import com.bside.grandmom.diaries.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
@Tag(name = "DIARY API", description = "일기 API")
@Slf4j
public class DiaryController {
    private final DiaryService diaryService;

    @Operation(
            summary = "로그인 API", description = "유저 정보를 저장합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 전송 성공"),
            @ApiResponse(responseCode = "400", description = "에러 코드 정의 필요")})
    @PostMapping("/image")
    public ResponseEntity<String> image(@RequestParam String imageUrl) {
        return diaryService.describeImage(imageUrl);
    }
}
