package com.bside.grandmom.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {
    private String code;
    private String message;
    private Object resData;

    public ResponseDto() {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    public ResponseDto(Object resData) {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
        this.resData = resData;
    }


    public ResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
        this.resData = null;  // resData는 null로 설정
    }

    public static ResponseDto databaseError() {
        return new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
    }


    public static ResponseDto success(Object resData) {
        return new ResponseDto(resData);
    }

    public static ResponseDto success() {
        return success(null);
    }

    public static ResponseDto error(String code, String message) {
        return new ResponseDto(code, message);
    }
}