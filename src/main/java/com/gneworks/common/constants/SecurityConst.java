package com.gneworks.common.constants;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 보안 관련 유틸리티
 *
 * @author y_ha
 */
@Component
@NoArgsConstructor
public class SecurityConst {

    @Value("${security.jwt.secret-key}")
    private String SYS_SECRET_KEY;

    @PostConstruct
    public void init() {
        SECRET_KEY = SYS_SECRET_KEY;
    }

    /**
     * JWT 시크릿 키
     */
    public static String SECRET_KEY;

    /**
     * Authorization 요청 헤더
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 액세스 토큰
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 리프레시 토큰
     */
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";

    /**
     * JWT 유효기간 7일
     */
    public static final Long EXPIRATION_TIME = 1000 * 60 * 60L * 24 * 7;

    /**
     * JWT 리프레시 유효기간 30일
     */
    public static final Long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60L * 24 * 30;

    /**
     * 게스트용 JWT 리프레시 유효기간 6시간
     */
    public static final Long REFRESH_EXPIRATION_GUEST_TIME = 1000 * 60 * 60L * 6;

    /**
     * 언어
     */
    public static final String LANGUAGE = "Accept-Language";

    /**
     * 인증 경로
     */
    public static final String VERIFY_AUTH_PATH = "/verify";

    /**
     * 재발급 경로
     */
    public static final String RECOVER_AUTH_PATH = "/auth/recover";
}



