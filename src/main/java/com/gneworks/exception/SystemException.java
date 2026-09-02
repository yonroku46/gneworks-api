package com.gneworks.exception;

/**
 * 시스템 오류용 예외 클래스
 *
 * @author y_ha
 */
public class SystemException extends RuntimeException {

    private final String errorCode;

    private final String errorDetail;

    public SystemException(String errorCode, String message, String errorDetail) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorDetail() {
        return this.errorDetail;
    }
}