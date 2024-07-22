package com.bside.grandmom.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }


    public static ResponseEntity<ResponseDto> success(Object resData) {
        ResponseDto responseBody = new ResponseDto(resData);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto responseBody = new ResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> error(String code, String message) {
        ResponseDto responseBody = new ResponseDto(code, message);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}