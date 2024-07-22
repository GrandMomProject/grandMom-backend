package com.bside.grandmom.common;

public enum StatusResCode {
    SUCCESS("0", "SUCCESS"),
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
