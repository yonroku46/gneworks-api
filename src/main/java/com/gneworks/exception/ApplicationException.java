package com.gneworks.exception;

import org.springframework.http.HttpStatus;

/**
 * 에러용 예외 클래스
 *
 * @author y_ha
 */
public class ApplicationException extends RuntimeException {

    private final HttpStatus status;

    private final String errorCode;

    public ApplicationException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
