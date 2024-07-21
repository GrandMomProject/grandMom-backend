package com.bside.grandmom.diaries.prompt;

public enum Prompt {
    DESCRIBE("What’s in this image? describe in Korea"),
    ;


    private final String prompt;

    Prompt(String message) {
        this.prompt = message;
    }


    public String getPrompt() {
        return prompt;
    }
}
