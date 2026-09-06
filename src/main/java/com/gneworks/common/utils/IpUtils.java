package com.gneworks.common.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 클라이언트 실제 IP 추출 유틸리티 (프록시, 로드밸런서, Cloudflare 환경 지원)
 */
public class IpUtils {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP",
            "CF-Connecting-IP"
    };

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "0.0.0.0";
        }

        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && !ipList.isEmpty() && !"unknown".equalsIgnoreCase(ipList)) {
                // X-Forwarded-For 등 여러 IP가 콤마로 연결된 경우 첫 번째가 클라이언트 원본 IP
                return ipList.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}