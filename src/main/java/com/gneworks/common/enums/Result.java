package com.gneworks.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 레스폰스 반환코드 ENUM
 * <PRE>
 * SUCCESS:0
 * FAILED:1
 * </PRE>
 *
 * @author y_ha
 */
@Getter
@AllArgsConstructor
public enum Result {
    SUCCESS(0),
    FAILED(1);

    private final int code;
}