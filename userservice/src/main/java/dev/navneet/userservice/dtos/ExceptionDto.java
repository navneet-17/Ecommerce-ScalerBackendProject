package dev.navneet.userservice.dtos;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ExceptionDto {
    private String message;

    public ExceptionDto(String message) {

        this.message = message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
//    private HttpStatus errorCode;
//    private String message;
//
//    public ExceptionDto(HttpStatus status, String message) {
//        this.errorCode = status;
//        this.message = message;
//    }
//
//    public void setErrorCode(HttpStatus errorCode) {
//        this.errorCode = errorCode;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

}
