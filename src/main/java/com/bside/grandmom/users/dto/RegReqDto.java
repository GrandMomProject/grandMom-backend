package com.bside.grandmom.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegReqDto {
    String uid;
    String did;
    char gender;
    char agreementYn;

    String loginType;
}
