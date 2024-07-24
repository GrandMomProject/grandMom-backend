package com.bside.grandmom.diaries.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageReqDto {
    private String type;
    private String diaryText;
}
