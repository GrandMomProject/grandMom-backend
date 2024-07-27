package com.bside.grandmom.diaries.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public record QuestionResDto(
        String type,
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = QuestionValue.class, name = "QUESTION"),
                @JsonSubTypes.Type(value = SummaryValue.class, name = "SUMMARY")
        }) Value value,
        int answerCount) {
    public interface Value {
    }

    public record QuestionValue(String question) implements Value {
    }

    public record SummaryValue(String summary1, String summary2, String summary3) implements Value {
        public static SummaryValue from(List<String> diaries) {
            return new SummaryValue(diaries.get(0), diaries.get(1), diaries.get(2));
        }
    }
}
