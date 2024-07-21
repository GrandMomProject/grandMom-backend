package com.bside.grandmom.diaries.prompt;

public enum Prompt {
    DESCRIBE("Describe the photo in Korean"),
    ;


    private final String prompt;

    Prompt(String prompt) {
        this.prompt = prompt;
    }


    public String getPrompt() {
        return prompt;
    }
}
