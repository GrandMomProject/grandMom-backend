package com.bside.grandmom.client.internalai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatHistoryRequestModel {
    @JsonProperty("배경")
    private String imageDescription;

    @JsonProperty("질의응답")
    private List<ChatHistoryModel> chatHistories;

    public ChatHistoryRequestModel(String imageDescription, List<ChatHistoryModel> chatHistories) {
        this.imageDescription = imageDescription;
        this.chatHistories = chatHistories;
    }

    public static class ChatHistoryModel {
        @JsonProperty("질문")
        private String question;

        @JsonProperty("대답")
        private String answer;

        public ChatHistoryModel(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }
}
