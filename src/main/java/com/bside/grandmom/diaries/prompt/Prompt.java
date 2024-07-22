package com.bside.grandmom.diaries.prompt;

public enum Prompt {
    DESCRIBE("사진에 대해 설명해봐 한국어로 말해"),
    ;


    private final String prompt;

    Prompt(String prompt) {
        this.prompt = prompt;
    }


    public String getPrompt() {
        return prompt;
    }
}
