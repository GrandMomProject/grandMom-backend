package com.bside.grandmom.users.service;

import com.bside.grandmom.client.oauth.KakaoClientService;
import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.users.domain.UserEntity;
import com.bside.grandmom.users.dto.RegReqDto;
import com.bside.grandmom.users.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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

    public ResponseDto<Void> memberReg(RegReqDto req) {
        try {
            // UID 또는 DID가 중복된 사용자가 있는지 확인
//            UserEntity existingUser = userRepository.findByDid(req.getDid());
            UserEntity existingUser = userRepository.findByDid(req.getUid());
            Date today = new Date();
            if (existingUser != null) {
                // 기존 사용자 업데이트 (빌더 패턴 사용)
                UserEntity updatedUser = existingUser.toBuilder()
                        .gender(req.getGender())
                        .uid(req.getUid())
                        .agreementYn(req.getAgreementYn())
                        .regDt(today)
                        .build();

                userRepository.save(updatedUser);

            } else {
                UserEntity user = UserEntity.builder()
                        .did(req.getDid())
                        .uid(req.getUid())
                        .gender(req.getGender())
                        .agreementYn(req.getAgreementYn())
                        .regDt(today)
                        .build();

                userRepository.save(user);
            }

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

    public String kakaoLogin() {
        return kakaoClientService.authorize();
    }

    public Map getKakaoProfile() {
        return kakaoClientService.getProfile();
    }

    public Map getKakaoToken(String code) {
        return kakaoClientService.getToken(code);
    }
}
