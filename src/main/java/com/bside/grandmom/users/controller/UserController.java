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

    @PostMapping("/reg")
    public ResponseEntity<ResponseDto> reg(@RequestBody RegReqDto req) {

        return userService.memberReg(req);
    }
}
