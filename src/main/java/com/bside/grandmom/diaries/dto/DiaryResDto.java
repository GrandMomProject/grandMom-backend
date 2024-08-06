package com.bside.grandmom.diaries.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryResDto {
    private Long detailNo;
    private String v1;
    private String v2;
    private String v3;
    private String img;
}
