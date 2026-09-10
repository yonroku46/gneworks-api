package com.gneworks.dao.entity;

import java.util.Date;

/**
 * Table: system_settings
 */
public class SystemSettings {
    /**
     * Column: setting_id
     * Type: TINYINT UNSIGNED(3)
     * Default value: 1
     * Remark: 고정 설정 ID (무조건 1)
     */
    private Byte settingId;

    /**
     * Column: contact_phone
     * Type: VARCHAR(30)
     * Default value: 010-6761-7665
     * Remark: 고객지원/비상 대표 전화번호
     */
    private String contactPhone;

    /**
     * Column: contact_email
     * Type: VARCHAR(100)
     * Default value: minkyu0026@nate.com
     * Remark: 고객지원 대표 이메일
     */
    private String contactEmail;

    /**
     * Column: notice_visible
     * Type: BIT
     * Default value: 1
     * Remark: 현장 안내사항 노출 여부 (1: 노출, 0: 숨김)
     */
    private Boolean noticeVisible;

    /**
     * Column: notice_title
     * Type: VARCHAR(255)
     * Default value: 현장 사진 촬영 및 보고서 작성 지침 안내
     * Remark: 안내사항 제목
     */
    private String noticeTitle;

    /**
     * Column: notice_date
     * Type: VARCHAR(20)
     * Remark: 게시 표시일자 (예: 2026.09.10)
     */
    private String noticeDate;

    /**
     * Column: update_time
     * Type: DATETIME
     * Default value: CURRENT_TIMESTAMP
     * Remark: 수정 일시
     */
    private Date updateTime;

    /**
     * Column: notice_content
     * Type: TEXT
     * Remark: 안내 상세 내용
     */
    private String noticeContent;

    public Byte getSettingId() {
        return settingId;
    }

    public void setSettingId(Byte settingId) {
        this.settingId = settingId;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail == null ? null : contactEmail.trim();
    }

    public Boolean getNoticeVisible() {
        return noticeVisible;
    }

    public void setNoticeVisible(Boolean noticeVisible) {
        this.noticeVisible = noticeVisible;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle == null ? null : noticeTitle.trim();
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate == null ? null : noticeDate.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent == null ? null : noticeContent.trim();
    }
}