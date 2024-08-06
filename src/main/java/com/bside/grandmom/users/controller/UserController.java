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
        /*// 인가 코드로 엑세스 토큰 받기
        Map accessToken = userService.getKakaoToken(code);

        // 유저 정보 가져오기
        Map result = userService.getKakaoProfile(accessToken.get("access_token").toString());

        // 유저 정보 db에 있는지 확인
        UserEntity chkUser = userService.checkUser(result.get("id").toString());
        long userNo;
        if(chkUser == null || chkUser.getUserNo() == null){
            // 유저 정보 저장
            userNo = userService.saveUser(result);
        } else {
            userNo = chkUser.getUserNo();
        }

        // 토큰 생성
        JwtProvider jwtProvider = new JwtProvider();
        String jwt = jwtProvider.createJwt(userNo);

        // 토큰 responose header에 넣어서 redirect 해주기
        response.addHeader("Authorization", "Bearer " + jwt);
        response.sendRedirect("http://localhost:3000/");*/
    }

    @PostMapping("/reg")
    public ResponseEntity<ResponseDto<Void>> reg(@RequestBody RegReqDto req, HttpServletResponse response) {
        return ResponseEntity.ok(userService.memberReg(req, response));
    }

    @GetMapping("/")
    public ResponseEntity<ResponseDto<?>> getUserInfo(HttpServletRequest request, @RequestBody GetUserInfoReqDto req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.getUser(req.getUid(), req.getDid());
        return ResponseEntity.ok(ResponseDto.success(user));
    }

}
