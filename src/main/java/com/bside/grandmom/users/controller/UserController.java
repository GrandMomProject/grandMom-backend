package com.bside.grandmom.users.controller;

import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.config.JwtProvider;
import com.bside.grandmom.users.domain.UserEntity;
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
import org.apache.catalina.User;
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
    }

    @GetMapping("/kakao/callback")
    public void kakaoCallback(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        String jwt = userService.kakaoCallbackProc(code, response);

        // 토큰 responose header에 넣어서 redirect 해주기
        response.addHeader("Authorization", "Bearer " + jwt);
        response.sendRedirect("http://localhost:3000/");

    }

    @PostMapping("/reg")
    public ResponseEntity<ResponseDto<?>> reg(@RequestBody RegReqDto req, HttpServletResponse response) {
        return ResponseEntity.ok(userService.memberReg(req, response));
    }

    @GetMapping("/")
    public ResponseEntity<ResponseDto<?>> getUserInfo(HttpServletRequest request, @RequestBody GetUserInfoReqDto req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.getUser(req.getUid(), req.getDid());
        return ResponseEntity.ok(ResponseDto.success(user));
    }

}
