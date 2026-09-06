package com.gneworks.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gneworks.common.utils.IpUtils;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 공개 API 봇 공격 및 무차별 호출(Brute-force) 방어용 IP 기반 Rate Limit 인터셉터
 */
@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Key: "IP:URI" -> Value: 요청 타임스탬프 큐
    private final Map<String, Queue<Long>> requestLogs = new ConcurrentHashMap<>();

    // 10분마다 캐시 자동 정리를 위한 타임스탬프
    private volatile long lastCleanupTime = System.currentTimeMillis();
    private static final long CLEANUP_INTERVAL = 10 * 60 * 1000L; // 10분

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // POST 요청에 대해서만 제한 적용
        if (!"POST".equalsIgnoreCase(method)) {
            return true;
        }

        int maxRequests;
        long windowMillis = 60 * 1000L; // 1분 (60초)

        if (uri.contains("/auth/login")) {
            // 로그인 무차별 대입 방어: 1분당 최대 5회
            maxRequests = 5;
        } else if (uri.contains("/contact/submit")) {
            // 문의 스팸 도배 방어: 1분당 최대 3회
            maxRequests = 3;
        } else {
            return true;
        }

        String clientIp = IpUtils.getClientIp(request);
        String limitKey = clientIp + ":" + (uri.contains("/auth/login") ? "LOGIN" : "CONTACT");

        // 주기적 오래된 메모리 정리
        cleanupOldEntriesIfNecessary();

        Queue<Long> timestamps = requestLogs.computeIfAbsent(limitKey, k -> new ConcurrentLinkedQueue<>());
        long now = System.currentTimeMillis();

        synchronized (timestamps) {
            // 1분(windowMillis) 이전 기록 제거
            while (!timestamps.isEmpty() && now - timestamps.peek() > windowMillis) {
                timestamps.poll();
            }

            if (timestamps.size() >= maxRequests) {
                log.warn("[RATE_LIMIT_EXCEEDED] Blocked IP: {}, URI: {}, Requests in 1m: {}", clientIp, uri, timestamps.size());

                response.setStatus(429); // 429 Too Many Requests
                response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Retry-After", "60");

                BaseResponse errorRes = new BaseResponse();
                errorRes.setHasErrors(true);
                List<Information> infoList = new ArrayList<>();
                infoList.add(new Information("TOO_MANY_REQUESTS", "단시간에 너무 많은 요청이 발생했습니다. 1분 후 다시 시도해 주세요."));
                errorRes.setInformations(infoList);

                response.getWriter().write(objectMapper.writeValueAsString(errorRes));
                return false;
            }

            timestamps.add(now);
        }

        return true;
    }

    private void cleanupOldEntriesIfNecessary() {
        long now = System.currentTimeMillis();
        if (now - lastCleanupTime > CLEANUP_INTERVAL) {
            lastCleanupTime = now;
            long expireThreshold = now - 60 * 1000L;
            requestLogs.entrySet().removeIf(entry -> {
                Queue<Long> queue = entry.getValue();
                synchronized (queue) {
                    while (!queue.isEmpty() && now - queue.peek() > 60 * 1000L) {
                        queue.poll();
                    }
                    return queue.isEmpty();
                }
            });
        }
    }
}