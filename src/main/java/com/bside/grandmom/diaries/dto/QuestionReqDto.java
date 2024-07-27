package com.bside.grandmom.diaries.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionReqDto {
    private String answer;
    private int answerCount;
}
