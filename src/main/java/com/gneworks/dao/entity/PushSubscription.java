package com.gneworks.dao.entity;

import jakarta.annotation.Generated;
import java.util.Date;

public class PushSubscription {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: push_subscription.subscription_id, Type: CHAR(27), Remark: 구독 고유 키 (KSUID)")
    private String subscriptionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: push_subscription.user_id, Type: CHAR(27), Remark: 사용자 키")
    private String userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: push_subscription.endpoint, Type: VARCHAR(700), Remark: 브라우저 푸시 엔드포인트 URL (UNIQUE)")
    private String endpoint;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: push_subscription.p256dh, Type: VARCHAR(255), Remark: 브라우저 공개키")
    private String p256dh;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: push_subscription.auth, Type: VARCHAR(255), Remark: 인증 시크릿")
    private String auth;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: push_subscription.user_agent, Type: VARCHAR(255), Remark: 접속 브라우저/기기 정보")
    private String userAgent;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: push_subscription.update_time, Type: DATETIME, Default value: CURRENT_TIMESTAMP, Remark: 수정 일시")
    private Date updateTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: push_subscription.create_time, Type: DATETIME, Default value: CURRENT_TIMESTAMP, Remark: 구독 일시")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getSubscriptionId() {
        return subscriptionId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId == null ? null : subscriptionId.trim();
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
    public String getEndpoint() {
        return endpoint;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint == null ? null : endpoint.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getP256dh() {
        return p256dh;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setP256dh(String p256dh) {
        this.p256dh = p256dh == null ? null : p256dh.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAuth() {
        return auth;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAuth(String auth) {
        this.auth = auth == null ? null : auth.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getUserAgent() {
        return userAgent;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? null : userAgent.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getUpdateTime() {
        return updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}