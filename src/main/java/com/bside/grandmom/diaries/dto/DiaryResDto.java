package com.bside.grandmom.diaries.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaryResDto {
    private String diaryID;
    private String summary1;
    private String summary2;
    private String summary3;
    private String voiceURL1;
    private String voiceURL2;
    private String voiceURL3;
}
