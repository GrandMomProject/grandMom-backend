package com.bside.grandmom.diaries.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResDto {
    private String diaryID;
    private String question;

    private String type;
}
