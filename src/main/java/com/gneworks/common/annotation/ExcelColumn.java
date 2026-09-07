package com.gneworks.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 엑셀 다운로드 시 포함할 DTO 필드에 부여하는 어노테이션.
 * 이 어노테이션이 부여된 필드만 순서대로 엑셀 컬럼으로 자동 변환됩니다.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    /**
     * 엑셀 헤더 표시명 (예: "현장명", "도로명 주소", "등록일자")
     */
    String header();

    /**
     * 컬럼 출력 순서 (낮은 숫자가 앞쪽에 배치됨)
     */
    int order() default 999;

    /**
     * 엑셀 열 너비 (기본 문자 수 단위, 예: 20)
     */
    int width() default 18;

    /**
     * 날짜/일시 포맷 (Date 타입인 경우 사용)
     */
    String dateFormat() default "yyyy-MM-dd HH:mm";

    /**
     * 텍스트 정렬: "LEFT", "CENTER", "RIGHT", "AUTO"
     */
    String align() default "AUTO";
}
