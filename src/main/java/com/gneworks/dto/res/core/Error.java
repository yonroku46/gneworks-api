package com.gneworks.dto.res.core;

import lombok.EqualsAndHashCode;

/**
 * 응답 에러 클래스
 *
 * @author y_ha
 */
@EqualsAndHashCode(callSuper = true)
public class Error extends Information {

    /**
     * 메서드 파라미터
     */
    private String parameter;

    /**
     * 에러 상세
     */
    private String errorDetail;

    /**
     * 생성자
     *
     * @param messageId 메시지 ID
     * @param message 메시지
     */
    public Error(String messageId, String message) {
        super(messageId, message);
    }

    /**
     * 생성자(BindingResult용)
     *
     * @param messageId 메시지 ID
     * @param message 메시지
     * @param parameter 파라미터
     */
    public Error(String messageId, String message, String parameter) {
        super(messageId, message);
        this.parameter = parameter;
    }

    /**
     * 생성자(스택 트레이스가 있는 경우)
     *
     * @param messageId 메시지 ID
     * @param message 메시지
     * @param parameter 파라미터
     * @param errorDetail 에러 상세
     */
    public Error(String messageId, String message, String parameter, String errorDetail) {
        super(messageId, message);
        this.errorDetail = errorDetail;
    }
}
