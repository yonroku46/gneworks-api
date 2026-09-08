package com.gneworks.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SseService {

    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String userId) {
        // timeout: 1 hour
        SseEmitter emitter = new SseEmitter(3600000L);

        if (userId != null) {
            emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        }

        Runnable removeEmitter = () -> {
            if (userId != null) {
                List<SseEmitter> list = emitters.get(userId);
                if (list != null) {
                    list.remove(emitter);
                    if (list.isEmpty()) {
                        emitters.remove(userId);
                    }
                }
            }
        };

        emitter.onCompletion(() -> {
            log.info("SSE connection completed for user: {}", userId);
            removeEmitter.run();
        });
        emitter.onTimeout(() -> {
            log.info("SSE connection timeout for user: {}", userId);
            emitter.complete();
            removeEmitter.run();
        });
        emitter.onError((e) -> {
            log.error("SSE connection error for user: {}: {}", userId, e.getMessage());
            emitter.complete();
            removeEmitter.run();
        });

        // Send initial connect event
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            log.error("Failed to send initial SSE event for user: {}", userId);
            removeEmitter.run();
        }

        return emitter;
    }

    public void sendNotification(String userId, Object data) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null && !userEmitters.isEmpty()) {
            for (SseEmitter emitter : userEmitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(data));
                    log.info("Sent SSE notification to user: {}", userId);
                } catch (IOException e) {
                    log.error("Failed to send SSE notification to user: {}", userId);
                    userEmitters.remove(emitter);
                }
            }
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }

    @Scheduled(fixedRate = 15000)
    public void heartbeat() {
        if (emitters.isEmpty())
            return;

        log.debug("[SSE] Sending heartbeat to {} users", emitters.size());
        emitters.forEach((userId, list) -> {
            for (SseEmitter emitter : list) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("ping")
                            .data("heartbeat"));
                } catch (IOException e) {
                    log.warn("[SSE] Removing dead emitter for user: {}", userId);
                    list.remove(emitter);
                }
            }
            if (list.isEmpty()) {
                emitters.remove(userId);
            }
        });
    }
}