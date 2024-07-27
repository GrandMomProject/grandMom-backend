package com.bside.grandmom.users.controller;

import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.diaries.dto.ImageReqDto;
import com.bside.grandmom.users.dto.RegReqDto;
import com.bside.grandmom.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 API")
@Slf4j
public class UserController {
    private final UserService userService;

    @Operation(summary = "이미지 전송 API", description = "사진을 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 전송 성공"),
            @ApiResponse(responseCode = "400", description = "에러 코드 정의 필요")
    })
    @PostMapping(value = "/reg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> reg(@RequestBody RegReqDto req) {

        return userService.memberReg(req);
    }
}
