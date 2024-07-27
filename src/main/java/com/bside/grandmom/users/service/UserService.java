package com.bside.grandmom.users.service;

import com.bside.grandmom.common.ResponseDto;
import com.bside.grandmom.users.domain.UserEntity;
import com.bside.grandmom.users.dto.RegReqDto;
import com.bside.grandmom.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<ResponseDto> memberReg(RegReqDto req) {
        try {
            UserEntity user = UserEntity.builder()
                    .did(req.getDid())
                    .uid(req.getUid())
                    .gender(req.getGender())
                    .agreementYn(req.getAgreementYn())
                    .build();
            userRepository.save(user);
            return ResponseDto.success();
        } catch (Error error) {
            return ResponseDto.error("999", "Failed Join.. Server Error");
        }


    }
}
