package com.gneworks.common.utils;

import com.gneworks.common.constants.SecurityConst;
import com.gneworks.common.id.KsuidGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

/**
 * JWT 관련 유틸리티
 */
@Component
public class JwtUtils {

    /**
     * 정상 로그인 시 JWT 생성
     *
     * @param ttlMillis JWT 유효기간
     * @param userId 사용자 ID
     * @return 생성된 JWT 문자열
     */
    public static String createJWT(long ttlMillis, String userId) {
        byte[] secretKeyAsBytes = SecurityConst.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        long nowMillis = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return buildJWT(ttlMillis, userId, claims);
    }

    /**
     * 게스트 로그인 시 JWT 생성
     *
     * @param ttlMillis JWT 유효기간
     * @param userId 사용자 ID
     * @param shopId 매장 ID
     * @param tableId 테이블 ID
     * @return 생성된 JWT 문자열
     */
    public static String createGuestJWT(long ttlMillis, String userId, String shopId, String tableId) {
        byte[] secretKeyAsBytes = SecurityConst.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        long nowMillis = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("shopId", shopId);
        claims.put("tableId", tableId);
        return buildJWT(ttlMillis, userId, claims);
    }



    /**
     * JWT 공통 생성 로직
     *
     * @param ttlMillis JWT 유효기간
     * @param userId 사용자 ID
     * @param claims JWT에 포함할 클레임
     * @return 생성된 JWT 문자열
     */
    private static String buildJWT(long ttlMillis, String userId, Map<String, Object> claims) {
        byte[] secretKeyAsBytes = SecurityConst.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        long nowMillis = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();

        JwtBuilder builder = Jwts.builder()
                .claims(claims)
                .id(KsuidGenerator.createId())
                .issuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .subject(userId)
                .signWith(Keys.hmacShaKeyFor(secretKeyAsBytes));

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            LocalDateTime exp = LocalDateTime.ofInstant(Instant.ofEpochMilli(expMillis), ZoneId.systemDefault());
            builder.expiration(Date.from(exp.atZone(ZoneId.systemDefault()).toInstant()));
        }
        return builder.compact();
    }

    /**
     * Token 복원
     *
     * @param token JWT 문자열
     * @return 복원된 Claims
     */
    public static Claims parseJWT(String token) {
        byte[] secretKeyAsBytes = SecurityConst.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyAsBytes);
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }
}