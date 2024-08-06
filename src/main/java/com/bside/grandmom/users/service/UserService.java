package com.bside.grandmom.users.service;

import com.bside.grandmom.client.oauth.KakaoClientService;
import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.config.JwtProvider;
import com.bside.grandmom.users.domain.UserEntity;
import com.bside.grandmom.users.dto.RegReqDto;
import com.bside.grandmom.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final KakaoClientService kakaoClientService;
    private final JwtProvider jwtProvider;

    public ResponseDto<Void> memberReg(RegReqDto req, HttpServletResponse response) {
        try {
            // UID 또는 DID가 중복된 사용자가 있는지 확인
//            UserEntity existingUser = userRepository.findByDid(req.getDid());
            UserEntity existingUser = userRepository.findByUid(req.getUid());
            UserEntity savedUser;
            Date today = new Date();
            if (existingUser != null) {
                // 기존 사용자 업데이트 (빌더 패턴 사용)
                UserEntity updatedUser = existingUser.toBuilder()
                        .gender(req.getGender())
                        .uid(req.getUid())
                        .agreementYn(req.getAgreementYn())
                        .lastDt(today)
                        .build();

                savedUser = userRepository.save(updatedUser);

            } else {
                UserEntity user = UserEntity.builder()
                        .did(req.getDid())
                        .uid(req.getUid())
                        .gender(req.getGender())
                        .agreementYn(req.getAgreementYn())
                        .regDt(today)
                        .build();

                savedUser = userRepository.save(user);
            }

            // 토큰 생성
            String jwt = generateAccessToken(savedUser.getUserNo());

            // 토큰을 response 헤더에 추가
            response.addHeader("Authorization", "Bearer " + jwt);

            return ResponseDto.success();
        } catch (Exception error) {
            log.error("Failed Join.. Server Error", error);
            return ResponseDto.error("999", "Failed Join.. Server Error");
        }
    }

    public UserEntity getUser(String uid, String did) {
        UserEntity user = userRepository.findByUidAndDid(uid, did)
                .orElseThrow(() -> new IllegalArgumentException("User not found. uid=" + uid + ", did=" + did));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(new Date());

        String userUpdDtStr = sdf.format(user.getUpdDt());
        try {
            Date today = sdf.parse(todayStr);
            Date userUpdDt = sdf.parse(userUpdDtStr);
            if (!userUpdDt.equals(today)) user.resetUseCnt();
            return user;
        } catch (ParseException e) {
            return null;
        }
    }

    public UserEntity getUser(long userNo) {
        return userRepository.findByUserNo(userNo);
    }

    public String kakaoLogin() {
        return kakaoClientService.authorize();
    }

    public Map getKakaoProfile(String accessToken) {
        return kakaoClientService.getProfile(accessToken);
    }

    public Map getKakaoToken(String code) {
        return kakaoClientService.getToken(code);
    }

    /*public long saveUser(Map result) {
        Date today = new Date();
        Map properties = (Map) result.get("properties");
        Map kakaoAcounts = (Map) result.get("kakao_account");
        UserEntity user = UserEntity.builder()
                .oauthId(result.get("id").toString())
                .nickname(properties.get("nickname").toString())
                .profileImg(properties.get("thumbnail_image").toString())
                .email(kakaoAcounts.get("email").toString())
                .loginType("KAKAO")
                .lastDt(today)
                .build();
        // UserEntity 저장 및 저장된 엔티티 반환
        UserEntity savedUser = userRepository.save(user);
        return savedUser.getUserNo();
    }*/

    public long saveUser(Map result) {
        Date today = new Date();
        Map properties = (Map) result.get("properties");
        Map kakaoAccounts = (Map) result.get("kakao_account");
        String oauthId = result.get("id").toString();

        // 유저 존재 여부 확인
        UserEntity existingUser = checkUser(oauthId);
        UserEntity user;

        if (existingUser != null) {
            // 기존 유저 정보 업데이트
            user = existingUser.toBuilder()
                    .nickname(properties.get("nickname").toString())
                    .profileImg(properties.get("thumbnail_image").toString())
                    .email(kakaoAccounts.get("email").toString())
                    .lastDt(today)
                    .build();
        } else {
            // 새로운 유저 정보 저장
            user = UserEntity.builder()
                    .oauthId(oauthId)
                    .nickname(properties.get("nickname").toString())
                    .profileImg(properties.get("thumbnail_image").toString())
                    .email(kakaoAccounts.get("email").toString())
                    .loginType("KAKAO")
                    .lastDt(today)
                    .build();
        }

        // UserEntity 저장 및 저장된 엔티티 반환
        UserEntity savedUser = userRepository.save(user);
        return savedUser.getUserNo();
    }

    public UserEntity checkUser(String id) {
        return userRepository.findByOauthId(id);
    }

    @Transactional
    public String kakaoCallbackProc(String code, HttpServletResponse response) throws IOException {
        try {
            // 인가 코드로 엑세스 토큰 받기
            Map accessToken = getKakaoToken(code);

            // 유저 정보 가져오기
            Map result = getKakaoProfile(accessToken.get("access_token").toString());

            // 유저 정보 저장 또는 업데이트
            long userNo = saveUser(result);

            // 토큰 생성
            String jwt = generateAccessToken(userNo);
            return jwt;


        } catch (Exception error) {
            log.error("Failed Join.. Server Error", error);
            throw new IOException(error);
        }

    }

    private String generateAccessToken(long userNo) {
        // 토큰 생성
        String jwt = jwtProvider.createJwt(userNo);
        return jwt;
    }
}
