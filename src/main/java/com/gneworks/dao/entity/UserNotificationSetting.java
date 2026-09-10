package com.gneworks.dao.entity;

import java.util.Date;

/**
 * Table: user_notification_setting
 */
public class UserNotificationSetting {
    /**
     * Column: user_id
     * Type: CHAR(27)
     * Remark: 사용자 키
     */
    private String userId;

    /**
     * Column: notify_web_push
     * Type: BIT
     * Default value: 1
     * Remark: 웹 푸시 마스터 On/Off
     */
    private Boolean notifyWebPush;

    /**
     * Column: notify_new_report
     * Type: BIT
     * Default value: 1
     * Remark: [관리자] 신규 보고서 알림
     */
    private Boolean notifyNewReport;

    /**
     * Column: notify_new_inquiry
     * Type: BIT
     * Default value: 1
     * Remark: [관리자] 신규 문의 알림
     */
    private Boolean notifyNewInquiry;

    /**
     * Column: notify_report_status
     * Type: BIT
     * Default value: 1
     * Remark: [작업자] 보고서 승인/반려 상태 알림
     */
    private Boolean notifyReportStatus;

    /**
     * Column: notify_inquiry_answer
     * Type: BIT
     * Default value: 1
     * Remark: [작업자] 문의 답변 등록 알림
     */
    private Boolean notifyInquiryAnswer;

    /**
     * Column: update_time
     * Type: DATETIME
     * Default value: CURRENT_TIMESTAMP
     * Remark: 최종 수정 일시
     */
    private Date updateTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Boolean getNotifyWebPush() {
        return notifyWebPush;
    }

    public void setNotifyWebPush(Boolean notifyWebPush) {
        this.notifyWebPush = notifyWebPush;
    }

    public Boolean getNotifyNewReport() {
        return notifyNewReport;
    }

    public void setNotifyNewReport(Boolean notifyNewReport) {
        this.notifyNewReport = notifyNewReport;
    }

    public Boolean getNotifyNewInquiry() {
        return notifyNewInquiry;
    }

    public void setNotifyNewInquiry(Boolean notifyNewInquiry) {
        this.notifyNewInquiry = notifyNewInquiry;
    }

    public Boolean getNotifyReportStatus() {
        return notifyReportStatus;
    }

    public void setNotifyReportStatus(Boolean notifyReportStatus) {
        this.notifyReportStatus = notifyReportStatus;
    }

    public Boolean getNotifyInquiryAnswer() {
        return notifyInquiryAnswer;
    }

    public void setNotifyInquiryAnswer(Boolean notifyInquiryAnswer) {
        this.notifyInquiryAnswer = notifyInquiryAnswer;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}