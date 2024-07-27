package com.bside.grandmom.client.internalai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AdditionalInterviewRequest {
    @JsonProperty("배경")
    private String imageDescription;

    @JsonProperty("질의응답")
    private List<ChatHistory> chatHistories;

    static class ChatHistory {
        @JsonProperty("질문")
        private String question;

        @JsonProperty("대답")
        private String answer;
    }
}
