package com.gneworks.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.enums.Roles;
import com.gneworks.common.id.KsuidGenerator;
import com.gneworks.common.utils.ResponseUtils;
import com.gneworks.dao.AppNotificationDao;
import com.gneworks.dao.PushSubscriptionDao;
import com.gneworks.dao.UserDao;
import com.gneworks.dao.UserNotificationSettingDao;
import com.gneworks.dao.entity.AppNotification;
import com.gneworks.dao.entity.PushSubscription;
import com.gneworks.dao.entity.User;
import com.gneworks.dao.entity.UserNotificationSetting;
import com.gneworks.dto.req.PushSubscriptionReq;
import com.gneworks.dto.req.UserNotificationSettingReq;
import com.gneworks.dto.res.ActionRes;
import com.gneworks.dto.res.ListRes;
import com.gneworks.dto.res.UserNotificationSettingRes;
import com.gneworks.dto.res.VapidKeyRes;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AppNotificationService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AppNotificationDao appNotificationDao;

    @Autowired
    private PushSubscriptionDao pushSubscriptionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SseService sseService;

    @Autowired
    private WebPushService webPushService;

    @Autowired
    private UserNotificationSettingDao userNotificationSettingDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(readOnly = true)
    public BaseResponse getNotifications(String userId) {
        List<AppNotification> list = appNotificationDao.selectByUserId(userId);
        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_GETTING_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_GETTING_SUCCESS, new String[] { "AppNotification" }, LocaleAspect.LOCALE)), new ListRes<>(list));
    }

    @Transactional
    public BaseResponse markAsRead(String userId, String notificationId) {
        ActionRes res = new ActionRes();
        AppNotification notification = appNotificationDao.selectByPrimaryKey(notificationId);

        if (notification == null || !notification.getUserId().equals(userId)) {
            return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.I_UPDATE_FAILED,
                    messageSource.getMessage(MessageIdConst.I_UPDATE_FAILED, null, LocaleAspect.LOCALE)));
        }

        notification.setIsRead(Boolean.TRUE);
        appNotificationDao.updateByPrimaryKey(notification);
        res.setSuccess(Boolean.TRUE);

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_UPDATE_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_UPDATE_SUCCESS, new String[] { "AppNotification" }, LocaleAspect.LOCALE)), res);
    }

    @Transactional
    public BaseResponse markAllAsRead(String userId) {
        ActionRes res = new ActionRes();
        appNotificationDao.updateAllAsReadByUserId(userId);
        res.setSuccess(Boolean.TRUE);

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_UPDATE_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_UPDATE_SUCCESS, new String[] { "AppNotification" }, LocaleAspect.LOCALE)), res);
    }

    public BaseResponse getVapidPublicKey() {
        String key = webPushService.getPublicKey();
        VapidKeyRes res = new VapidKeyRes(key);
        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_GETTING_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_GETTING_SUCCESS, new String[] { "VapidPublicKey" }, LocaleAspect.LOCALE)), res);
    }

    @Transactional
    public BaseResponse subscribePush(String userId, PushSubscriptionReq req) {
        ActionRes res = new ActionRes();
        if (req == null || req.getEndpoint() == null || req.getEndpoint().isBlank()) {
            return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.I_SAVE_FAILED,
                    messageSource.getMessage(MessageIdConst.I_SAVE_FAILED, null, LocaleAspect.LOCALE)));
        }

        PushSubscription subscription = new PushSubscription();
        subscription.setSubscriptionId(KsuidGenerator.createId());
        subscription.setUserId(userId);
        subscription.setEndpoint(req.getEndpoint());
        subscription.setP256dh(req.getP256dh());
        subscription.setAuth(req.getAuth());
        subscription.setUserAgent(req.getUserAgent());
        subscription.setCreateTime(new Date());
        subscription.setUpdateTime(new Date());

        pushSubscriptionDao.saveOrUpdate(subscription);
        res.setSuccess(Boolean.TRUE);

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_SAVE_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_SAVE_SUCCESS, new String[] { "PushSubscription" }, LocaleAspect.LOCALE)), res);
    }

    @Transactional
    public BaseResponse unsubscribePush(String userId, String endpoint) {
        ActionRes res = new ActionRes();
        if (endpoint != null && !endpoint.isBlank()) {
            pushSubscriptionDao.deleteByUserIdAndEndpoint(userId, endpoint);
        }
        res.setSuccess(Boolean.TRUE);
        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_DELETE_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_DELETE_SUCCESS, new String[] { "PushSubscription" }, LocaleAspect.LOCALE)), res);
    }

    public BaseResponse sendTestNotification(String userId) {
        ActionRes res = new ActionRes();
        if (userId != null && !userId.isBlank()) {
            try {
                List<PushSubscription> subscriptions = pushSubscriptionDao.selectByUserId(userId);
                if (subscriptions != null && !subscriptions.isEmpty()) {
                    String payloadJson = createPayloadJson("테스트 알림", "웹 푸시 알림이 정상적으로 연동되었습니다.", "/portal");
                    for (PushSubscription sub : subscriptions) {
                        webPushService.sendPushNotification(sub.getEndpoint(), sub.getP256dh(), sub.getAuth(), payloadJson);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to send test WebPush to user {}: {}", userId, e.getMessage(), e);
            }
        }
        res.setSuccess(Boolean.TRUE);
        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_SAVE_SUCCESS, "테스트 알림이 발송되었습니다."), res);
    }

    private boolean isAdmin(String userId) {
        if (userId == null || userId.isBlank()) return false;
        User user = userDao.findUser(userId);
        return user != null && user.getRoleId() != null && user.getRoleId() == Roles.ROOT.getValue();
    }

    /**
     * 사용자 알림 설정 조회
     * GET /notification/settings
     */
    @Transactional(readOnly = true)
    public BaseResponse getUserNotificationSettings(String userId) {
        if (userId == null || userId.isBlank()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER", "LOGIN_REQUIRED"));
        }

        boolean admin = isAdmin(userId);
        UserNotificationSetting setting = userNotificationSettingDao.selectByPrimaryKey(userId);
        if (setting == null) {
            UserNotificationSettingRes res = UserNotificationSettingRes.builder()
                    .userId(userId)
                    .notifyWebPush(Boolean.FALSE)
                    .notifyNewReport(admin ? Boolean.TRUE : Boolean.FALSE)
                    .notifyNewInquiry(admin ? Boolean.TRUE : Boolean.FALSE)
                    .notifyReportStatus(!admin ? Boolean.TRUE : Boolean.FALSE)
                    .notifyInquiryAnswer(!admin ? Boolean.TRUE : Boolean.FALSE)
                    .build();
            return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_GETTING_SUCCESS, "알림 설정을 조회했습니다."), res);
        }

        UserNotificationSettingRes res = UserNotificationSettingRes.builder()
                .userId(setting.getUserId())
                .notifyWebPush(setting.getNotifyWebPush() != null ? setting.getNotifyWebPush() : Boolean.FALSE)
                .notifyNewReport(admin ? (setting.getNotifyNewReport() != null ? setting.getNotifyNewReport() : Boolean.TRUE) : Boolean.FALSE)
                .notifyNewInquiry(admin ? (setting.getNotifyNewInquiry() != null ? setting.getNotifyNewInquiry() : Boolean.TRUE) : Boolean.FALSE)
                .notifyReportStatus(!admin ? (setting.getNotifyReportStatus() != null ? setting.getNotifyReportStatus() : Boolean.TRUE) : Boolean.FALSE)
                .notifyInquiryAnswer(!admin ? (setting.getNotifyInquiryAnswer() != null ? setting.getNotifyInquiryAnswer() : Boolean.TRUE) : Boolean.FALSE)
                .build();

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_GETTING_SUCCESS, "알림 설정을 조회했습니다."), res);
    }

    /**
     * 사용자 알림 설정 수정
     * PUT /notification/settings
     */
    @Transactional
    public BaseResponse updateUserNotificationSettings(String userId, UserNotificationSettingReq req) {
        if (userId == null || userId.isBlank()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER", "LOGIN_REQUIRED"));
        }

        boolean admin = isAdmin(userId);
        UserNotificationSetting setting = userNotificationSettingDao.selectByPrimaryKey(userId);
        if (setting == null) {
            setting = new UserNotificationSetting();
            setting.setUserId(userId);
            setting.setNotifyWebPush(req != null && req.getNotifyWebPush() != null ? req.getNotifyWebPush() : Boolean.FALSE);

            if (admin) {
                setting.setNotifyNewReport(req != null && req.getNotifyNewReport() != null ? req.getNotifyNewReport() : Boolean.TRUE);
                setting.setNotifyNewInquiry(req != null && req.getNotifyNewInquiry() != null ? req.getNotifyNewInquiry() : Boolean.TRUE);
                setting.setNotifyReportStatus(Boolean.FALSE);
                setting.setNotifyInquiryAnswer(Boolean.FALSE);
            } else {
                setting.setNotifyNewReport(Boolean.FALSE);
                setting.setNotifyNewInquiry(Boolean.FALSE);
                setting.setNotifyReportStatus(req != null && req.getNotifyReportStatus() != null ? req.getNotifyReportStatus() : Boolean.TRUE);
                setting.setNotifyInquiryAnswer(req != null && req.getNotifyInquiryAnswer() != null ? req.getNotifyInquiryAnswer() : Boolean.TRUE);
            }

            setting.setUpdateTime(new Date());
            userNotificationSettingDao.insert(setting);
        } else {
            if (req != null) {
                if (req.getNotifyWebPush() != null) setting.setNotifyWebPush(req.getNotifyWebPush());

                if (admin) {
                    if (req.getNotifyNewReport() != null) setting.setNotifyNewReport(req.getNotifyNewReport());
                    if (req.getNotifyNewInquiry() != null) setting.setNotifyNewInquiry(req.getNotifyNewInquiry());
                    setting.setNotifyReportStatus(Boolean.FALSE);
                    setting.setNotifyInquiryAnswer(Boolean.FALSE);
                } else {
                    setting.setNotifyNewReport(Boolean.FALSE);
                    setting.setNotifyNewInquiry(Boolean.FALSE);
                    if (req.getNotifyReportStatus() != null) setting.setNotifyReportStatus(req.getNotifyReportStatus());
                    if (req.getNotifyInquiryAnswer() != null) setting.setNotifyInquiryAnswer(req.getNotifyInquiryAnswer());
                }
            }
            setting.setUpdateTime(new Date());
            userNotificationSettingDao.updateByPrimaryKey(setting);
        }

        ActionRes res = new ActionRes();
        res.setSuccess(Boolean.TRUE);
        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_SAVE_SUCCESS, "알림 설정이 저장되었습니다."), res);
    }

    /**
     * 특정 사용자에게 통합 알림 발송 (DB 저장 + SSE + Web Push)
     */
    @Transactional
    public void sendNotificationToUser(String userId, String title, String message, String url, String iconType) {
        if (userId == null || userId.isBlank()) return;

        // 1. DB 알림 테이블 저장
        String storedMessage = (url != null && !url.trim().isEmpty())
                ? message + "\n<!--link:" + url.trim() + "-->"
                : message;

        AppNotification notification = new AppNotification();
        notification.setAppNotificationId(KsuidGenerator.createId());
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setMessage(storedMessage);
        notification.setIconType(iconType != null ? iconType : "LOGO");
        notification.setIsRead(Boolean.FALSE);
        notification.setCreateTime(new Date());

        try {
            appNotificationDao.insert(notification);
        } catch (Exception e) {
            log.error("Failed to insert app_notification for user {}: {}", userId, e.getMessage(), e);
        }

        // 2. SSE 실시간 브로드캐스트
        try {
            sseService.sendNotification(userId, notification);
        } catch (Exception e) {
            log.error("Failed to send SSE notification to user {}: {}", userId, e.getMessage(), e);
        }

        // 3. Web Push 발송 (해당 사용자의 설정 확인 후 등록 기기 발송)
        try {
            boolean shouldWebPush = true;
            UserNotificationSetting setting = userNotificationSettingDao.selectByPrimaryKey(userId);
            if (setting != null) {
                if (Boolean.FALSE.equals(setting.getNotifyWebPush())) {
                    shouldWebPush = false;
                } else {
                    boolean isReportStatus = (title != null && title.contains("보고서")) || "REPORT_STATUS".equalsIgnoreCase(iconType);
                    boolean isInquiryAnswer = (title != null && title.contains("문의")) || "INQUIRY_ANSWER".equalsIgnoreCase(iconType);

                    if (isReportStatus && Boolean.FALSE.equals(setting.getNotifyReportStatus())) {
                        shouldWebPush = false;
                    } else if (isInquiryAnswer && Boolean.FALSE.equals(setting.getNotifyInquiryAnswer())) {
                        shouldWebPush = false;
                    }
                }
            }

            if (shouldWebPush) {
                List<PushSubscription> subscriptions = pushSubscriptionDao.selectByUserId(userId);
                if (subscriptions != null && !subscriptions.isEmpty()) {
                    String payloadJson = createPayloadJson(title, message, url);
                    for (PushSubscription sub : subscriptions) {
                        webPushService.sendPushNotification(sub.getEndpoint(), sub.getP256dh(), sub.getAuth(), payloadJson);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to send WebPush to user {}: {}", userId, e.getMessage(), e);
        }
    }

    /**
     * 관리자 전원에게 통합 알림 발송 (모든 관리자 DB 저장 + SSE + 모든 관리자 기기 Web Push)
     */
    @Transactional
    public void sendNotificationToAdmins(String title, String message, String url, String iconType) {
        // 1. 모든 관리자(role_id = 9) DB 저장 및 SSE 전송
        try {
            List<User> admins = userDao.selectUsersByRoleId(Roles.ROOT.getValue());
            if (admins != null && !admins.isEmpty()) {
                String storedMessage = (url != null && !url.trim().isEmpty())
                ? message + "\n<!--link:" + url.trim() + "-->"
                : message;

                for (User admin : admins) {
                    AppNotification notification = new AppNotification();
                    notification.setAppNotificationId(KsuidGenerator.createId());
                    notification.setUserId(admin.getUserId());
                    notification.setTitle(title);
                    notification.setMessage(storedMessage);
                    notification.setIconType(iconType != null ? iconType : "LOGO");
                    notification.setIsRead(Boolean.FALSE);
                    notification.setCreateTime(new Date());

                    try {
                        appNotificationDao.insert(notification);
                        sseService.sendNotification(admin.getUserId(), notification);
                    } catch (Exception e) {
                        log.error("Failed to save notification for admin {}: {}", admin.getUserId(), e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to query admins for notifications: {}", e.getMessage(), e);
        }

        // 2. 관리자 권한을 가진 브라우저 기기 엔드포인트로 설정 확인 후 Web Push 발송
        try {
            List<PushSubscription> adminSubs = pushSubscriptionDao.selectAdminSubscriptions();
            if (adminSubs != null && !adminSubs.isEmpty()) {
                String payloadJson = createPayloadJson(title, message, url);
                Map<String, UserNotificationSetting> settingCache = new HashMap<>();

                boolean isNewReport = (title != null && title.contains("보고서")) || (url != null && url.contains("/work")) || "NEW_REPORT".equalsIgnoreCase(iconType);
                boolean isNewInquiry = (title != null && title.contains("문의")) || (url != null && url.contains("/inquiries")) || "NEW_INQUIRY".equalsIgnoreCase(iconType);

                for (PushSubscription sub : adminSubs) {
                    String adminId = sub.getUserId();
                    UserNotificationSetting setting = settingCache.computeIfAbsent(adminId, id -> userNotificationSettingDao.selectByPrimaryKey(id));
                    boolean shouldPush = true;
                    if (setting != null) {
                        if (Boolean.FALSE.equals(setting.getNotifyWebPush())) {
                            shouldPush = false;
                        } else if (isNewReport && Boolean.FALSE.equals(setting.getNotifyNewReport())) {
                            shouldPush = false;
                        } else if (isNewInquiry && Boolean.FALSE.equals(setting.getNotifyNewInquiry())) {
                            shouldPush = false;
                        }
                    }

                    if (shouldPush) {
                        webPushService.sendPushNotification(sub.getEndpoint(), sub.getP256dh(), sub.getAuth(), payloadJson);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to broadcast WebPush to admins: {}", e.getMessage(), e);
        }
    }

    private String createPayloadJson(String title, String body, String url) {
        try {
            Map<String, Object> payloadMap = new HashMap<>();
            payloadMap.put("title", title);
            payloadMap.put("body", body);
            payloadMap.put("icon", "/favicon.ico");
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("url", url != null ? url : "/");
            payloadMap.put("data", dataMap);
            return objectMapper.writeValueAsString(payloadMap);
        } catch (Exception e) {
            log.error("Failed to serialize push payload: {}", e.getMessage());
            return "{\"title\":\"" + title + "\",\"body\":\"" + body + "\"}";
        }
    }
}
