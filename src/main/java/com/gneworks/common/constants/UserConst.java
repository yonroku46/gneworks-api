package com.gneworks.common.constants;

import lombok.NoArgsConstructor;


/**
 * 사용자 관련 유틸리티
 *
 * @author y_ha
 */
@NoArgsConstructor
public class UserConst {

    /**
     * 게스트 사용자를 식별하기 위한 사용자 ID 접두사
     */
    public static final String GUEST_PREFIX = "guest-";

    /**
     * 게스트 사용자 전용 이메일 주소 도메인
     *
     * ※ 실제 존재하지 않는 도메인(RFC 2606)을 사용하여,
     *    이메일 발송 및 도메인 악용을 방지하기 위한 목적으로 사용함
     */
    public static final String GUEST_EMAIL_DOMAIN = "@guest.invalid";
}



