package com.gneworks.api.service;

import com.gneworks.dao.PushSubscriptionDao;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Security;

@Service
@Slf4j
public class WebPushService {

    @Autowired
    private PushSubscriptionDao pushSubscriptionDao;

    @Value("${vapid.publicKey}")
    private String publicKey;

    @Value("${vapid.privateKey}")
    private String privateKey;

    @Value("${vapid.subject}")
    private String subject;

    private PushService pushService;

    @PostConstruct
    public void init() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        try {
            if (publicKey != null && !publicKey.isBlank() && privateKey != null && !privateKey.isBlank()) {
                pushService = new PushService(publicKey, privateKey, subject);
                log.info("WebPushService successfully initialized with VAPID keys.");
            } else {
                log.warn("WebPushService: VAPID keys are empty.");
            }
        } catch (Exception e) {
            log.error("Failed to initialize WebPushService: {}", e.getMessage(), e);
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

    public boolean sendPushNotification(String endpoint, String p256dh, String auth, String payload) {
        if (pushService == null) {
            log.error("PushService is not initialized. Cannot send push notification.");
            return false;
        }
        try {
            Subscription subscription = new Subscription(endpoint, new Subscription.Keys(p256dh, auth));
            Notification notification = new Notification(subscription, payload);
            var response = pushService.send(notification);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("Push notification sent to {}. Status code: {}", endpoint, statusCode);

            if (statusCode == 404 || statusCode == 410) {
                log.info("Push subscription expired ({}), removing endpoint: {}", statusCode, endpoint);
                pushSubscriptionDao.deleteByEndpoint(endpoint);
                return false;
            }

            return statusCode >= 200 && statusCode < 300;
        } catch (Exception e) {
            log.error("Failed to send WebPush notification to endpoint {}: {}", endpoint, e.getMessage());
            // If the error indicates gone/invalid subscription, remove endpoint
            if (e.getMessage() != null && (e.getMessage().contains("410") || e.getMessage().contains("404"))) {
                try {
                    pushSubscriptionDao.deleteByEndpoint(endpoint);
                } catch (Exception ex) {
                    log.error("Failed to delete expired endpoint: {}", ex.getMessage());
                }
            }
            return false;
        }
    }
}
