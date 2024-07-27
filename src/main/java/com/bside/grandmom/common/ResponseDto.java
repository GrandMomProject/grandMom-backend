package com.bside.grandmom.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private String code;
    private String message;
    private T resData;

    public ResponseDto() {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    public ResponseDto(T resData) {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
        this.resData = resData;
    }


    public ResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
        this.resData = null;  // resData는 null로 설정
    }

    public static ResponseDto<Void> databaseError() {
        return new ResponseDto<>(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
    }


    public static <T> ResponseDto<T> success(T resData) {
        return new ResponseDto<>(resData);
    }

    public static ResponseDto<Void> success() {
        return success(null);
    }

    public static ResponseDto<Void> error(String code, String message) {
        return new ResponseDto<>(code, message);
    }
}