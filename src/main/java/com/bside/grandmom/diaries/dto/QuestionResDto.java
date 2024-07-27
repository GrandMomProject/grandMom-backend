package com.bside.grandmom.diaries.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResDto {
    private String type;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = QuestionValue.class, name = "QUESTION"),
            @JsonSubTypes.Type(value = SummaryValue.class, name = "SUMMARY")
    })
    private Value value;
    private int answerCount;

    // getters and setters

    @Data
    public static class Value {
        // Common fields, if any
    }
    @Data
    public static class QuestionValue extends Value {
        private String question;

        // getters and setters
    }
    @Data
    public static class SummaryValue extends Value {
        private String summary1;
        private String summary2;
        private String summary3;

        // getters and setters
    }

}
