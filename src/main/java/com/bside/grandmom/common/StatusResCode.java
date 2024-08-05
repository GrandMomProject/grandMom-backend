package com.bside.grandmom.common;

public enum StatusResCode {
    SUCCESS("0", "SUCCESS"),
    INVALID_TOKEN("1001", "유효하지 않은 토큰입니다."),
    ;
    private final String code;
    private final String message;

    StatusResCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
