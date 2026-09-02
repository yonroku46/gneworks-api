package com.gneworks.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String userId) {
        // timeout: 1 hour
        SseEmitter emitter = new SseEmitter(3600000L);

        if (userId != null) {
            emitters.put(userId, emitter);
        }

        emitter.onCompletion(() -> {
            log.info("SSE connection completed for user: {}", userId);
            if (userId != null) {
                emitters.remove(userId);
            }
        });
        emitter.onTimeout(() -> {
            log.info("SSE connection timeout for user: {}", userId);
            emitter.complete();
            if (userId != null) {
                emitters.remove(userId);
            }
        });
        emitter.onError((e) -> {
            log.error("SSE connection error for user: {}: {}", userId, e.getMessage());
            emitter.complete();
            if (userId != null) {
                emitters.remove(userId);
            }
        });

        // Send initial connect event
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            log.error("Failed to send initial SSE event for user: {}", userId);
            emitters.remove(userId);
        }

        return emitter;
    }

    public void sendNotification(String userId, Object data) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(data));
                log.info("Sent SSE notification to user: {}", userId);
            } catch (IOException e) {
                log.error("Failed to send SSE notification to user: {}", userId);
                emitters.remove(userId);
            }
        }
    }

    @Scheduled(fixedRate = 15000)
    public void heartbeat() {
        if (emitters.isEmpty())
            return;

        log.debug("[SSE] Sending heartbeat to {} users", emitters.size());
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("ping")
                        .data("heartbeat"));
            } catch (IOException e) {
                log.warn("[SSE] Removing dead emitter for user: {}", userId);
                emitters.remove(userId);
            }
        });
    }
}