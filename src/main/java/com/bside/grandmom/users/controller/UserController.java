package com.bside.grandmom.users.controller;

import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.users.domain.UserEntity;
import com.bside.grandmom.users.dto.GetUserInfoReqDto;
import com.bside.grandmom.users.dto.RegReqDto;
import com.bside.grandmom.users.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 API")
@Slf4j
public class UserController {
    private final UserService userService;
    @GetMapping("/kakao/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String url = userService.kakaoLogin();
        response.sendRedirect(url);
//        response.sendRedirect("https://kauth.kakao.com/oauth/authorize?client_id=1839b2d52be1552b24b1eebb70993e6f&redirect_uri=https://webhook.site/686eef94-6862-447e-8e9f-b69c2784043c&response_type=code");
    }

    @GetMapping("/kakao/callback")
    public void kakaoCallback(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        System.out.println("code:" + code);
        // 인가 코드로 엑세스 토큰 받기
        Map accessToken = userService.getKakaoToken(code);
        System.out.println("accessToken:" + accessToken);

        // 유저 정보 가져오기
        Map result = userService.getKakaoProfile();
        // 유저 정보 저장

        // 토큰 생성

        // 토큰 responose header에 넣어서 redirect 해주기
        response.addHeader("Bearer", code);
        response.sendRedirect("/");
    }

    @PostMapping("/reg")
    public ResponseEntity<ResponseDto<Void>> reg(@RequestBody RegReqDto req) {
        return ResponseEntity.ok(userService.memberReg(req));
    }

    @GetMapping("/")
    public ResponseEntity<ResponseDto<?>> getUserInfo(HttpServletRequest request, @RequestBody GetUserInfoReqDto req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.getUser(req.getUid(), req.getDid());
        return ResponseEntity.ok(ResponseDto.success(user));
    }

}
