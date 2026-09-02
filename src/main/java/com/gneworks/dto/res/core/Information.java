package com.gneworks.dto.res.core;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  응답 정보 클래스
 *
 * @author y_ha
 */
@Data
@AllArgsConstructor
public class Information {

    /**
     * 메시지 ID
     */
    private String messageId;

    /**
     * 메시지
     */
    private String message;
}