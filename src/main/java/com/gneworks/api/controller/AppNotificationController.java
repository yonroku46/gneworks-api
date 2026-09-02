package com.gneworks.api.controller;

import com.gneworks.api.controller.base.BaseController;
import com.gneworks.api.service.AppNotificationService;
import com.gneworks.api.service.SseService;
import com.gneworks.aspect.attribute.CheckToken;
import com.gneworks.dto.res.core.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notification")
@Slf4j
@CheckToken
public class AppNotificationController extends BaseController {

    @Autowired
    private AppNotificationService appNotificationService;

    @Autowired
    private SseService sseService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        response.setHeader("Cache-Control", "no-cache, no-transform");
        response.setHeader("X-Accel-Buffering", "no");

        String userId = getCurrentUserId();
        log.info("[SSE] Subscription request for user: {}", userId);
        if (userId == null) {
            log.warn("[SSE] Subscription failed: User ID is null");
        }
        return sseService.subscribe(userId);
    }

    @GetMapping("/list")
    public BaseResponse getNotifications() {
        return appNotificationService.getNotifications(getCurrentUserId());
    }

    @PatchMapping("/read")
    public BaseResponse markAsRead(@RequestParam String notificationId) {
        return appNotificationService.markAsRead(getCurrentUserId(), notificationId);
    }

    @PatchMapping("/read-all")
    public BaseResponse markAllAsRead() {
        return appNotificationService.markAllAsRead(getCurrentUserId());
    }
}