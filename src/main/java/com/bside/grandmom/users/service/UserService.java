package com.bside.grandmom.users.service;

import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.users.domain.UserEntity;
import com.bside.grandmom.users.dto.RegReqDto;
import com.bside.grandmom.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<ResponseDto> memberReg(RegReqDto req) {
        try {
            // UID 또는 DID가 중복된 사용자가 있는지 확인
            UserEntity existingUser = userRepository.findByDid(req.getDid());
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
        } catch (Error error) {
            return ResponseDto.error("999", "Failed Join.. Server Error");
        }


    }
}
