package com.gneworks.dao.entity;

import jakarta.annotation.Generated;
import java.util.Date;

public class AppNotification {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: app_notification.app_notification_id, Type: CHAR(27), Remark: 앱 알림 고유 키")
    private String appNotificationId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: app_notification.user_id, Type: CHAR(27), Remark: 알림 대상 사용자 키")
    private String userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: app_notification.title, Type: VARCHAR(100), Remark: 알림 제목")
    private String title;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: app_notification.is_read, Type: TINYINT(3), Default value: 0, Remark: 읽음 여부")
    private boolean isRead;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: app_notification.icon_type, Type: VARCHAR(20), Remark: 아이콘 타입(LOGO, AVATAR)")
    private String iconType;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: app_notification.create_time, Type: DATETIME, Default value: CURRENT_TIMESTAMP, Remark: 생성 일시")
    private Date createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: app_notification.message, Type: TEXT, Remark: 알림 메시지")
    private String message;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAppNotificationId() {
        return appNotificationId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAppNotificationId(String appNotificationId) {
        this.appNotificationId = appNotificationId == null ? null : appNotificationId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getUserId() {
        return userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getTitle() {
        return title;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public boolean getIsRead() {
        return isRead;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getIconType() {
        return iconType;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIconType(String iconType) {
        this.iconType = iconType == null ? null : iconType.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getMessage() {
        return message;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }
}