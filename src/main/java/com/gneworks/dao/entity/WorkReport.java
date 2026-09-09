package com.gneworks.dao.entity;

import java.util.Date;

/**
 * Table: work_report
 */
public class WorkReport {
    /**
     * Column: report_id
     * Type: CHAR(27)
     * Remark: 보고서 고유 키
     */
    private String reportId;

    /**
     * Column: household_id
     * Type: CHAR(27)
     * Remark: 세대 고유 키
     */
    private String householdId;

    /**
     * Column: site_id
     * Type: CHAR(27)
     * Remark: 현장 고유 키
     */
    private String siteId;

    /**
     * Column: user_id
     * Type: CHAR(27)
     * Remark: 작성 작업자 고유 키
     */
    private String userId;

    /**
     * Column: dong
     * Type: VARCHAR(20)
     * Remark: 동 (예: 101동)
     */
    private String dong;

    /**
     * Column: ho
     * Type: VARCHAR(20)
     * Remark: 호수 (예: 101호)
     */
    private String ho;

    /**
     * Column: head_name
     * Type: VARCHAR(50)
     * Remark: 세대주 성명
     */
    private String headName;

    /**
     * Column: install_date
     * Type: DATE
     * Remark: 설치 일자
     */
    private Date installDate;

    /**
     * Column: report_time
     * Type: DATETIME
     * Remark: 보고서 작성/제출 일시
     */
    private Date reportTime;

    /**
     * Column: reporter_name
     * Type: VARCHAR(50)
     * Remark: 작업자(보고자) 성명
     */
    private String reporterName;

    /**
     * Column: confirmer_name
     * Type: VARCHAR(50)
     * Remark: 확인자 성명 (세대주 또는 대리인)
     */
    private String confirmerName;

    /**
     * Column: confirmer_signature
     * Type: VARCHAR(500)
     * Remark: 확인자 전자서명
     */
    private String confirmerSignature;

    /**
     * Column: photo_door
     * Type: VARCHAR(500)
     * Remark: 1. 신주소 보이는 대문 사진
     */
    private String photoDoor;

    /**
     * Column: photo_before1
     * Type: VARCHAR(500)
     * Remark: 2. 단독경보형감지기 보급 전 ①
     */
    private String photoBefore1;

    /**
     * Column: photo_after1
     * Type: VARCHAR(500)
     * Remark: 3. 단독경보형감지기 보급 후 ①
     */
    private String photoAfter1;

    /**
     * Column: photo_before2
     * Type: VARCHAR(500)
     * Remark: 4. 단독경보형감지기 보급 전 ②
     */
    private String photoBefore2;

    /**
     * Column: photo_after2
     * Type: VARCHAR(500)
     * Remark: 5. 단독경보형감지기 보급 후 ②
     */
    private String photoAfter2;

    /**
     * Column: status
     * Type: VARCHAR(20)
     * Default value: PENDING
     * Remark: 진행 상태 (PENDING, COMPLETED, REJECTED)
     */
    private String status;

    /**
     * Column: remarks
     * Type: VARCHAR(500)
     * Remark: 현장 특이사항 및 비고
     */
    private String remarks;

    /**
     * Column: last_update
     * Type: DATETIME
     * Default value: CURRENT_TIMESTAMP
     * Remark: 최종 수정 일시
     */
    private Date lastUpdate;

    /**
     * Column: create_time
     * Type: DATETIME
     * Default value: CURRENT_TIMESTAMP
     * Remark: 생성 일시
     */
    private Date createTime;

    /**
     * Column: delete_flg
     * Type: BIT
     * Default value: 0
     * Remark: 삭제 여부
     */
    private Boolean deleteFlg;

    /**
     * Column: fix_reason
     * Type: TEXT
     * Remark: 반려/보완 요청 사유
     */
    private String fixReason;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId == null ? null : reportId.trim();
    }

    public String getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId == null ? null : householdId.trim();
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong == null ? null : dong.trim();
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho == null ? null : ho.trim();
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName == null ? null : headName.trim();
    }

    public Date getInstallDate() {
        return installDate;
    }

    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName == null ? null : reporterName.trim();
    }

    public String getConfirmerName() {
        return confirmerName;
    }

    public void setConfirmerName(String confirmerName) {
        this.confirmerName = confirmerName == null ? null : confirmerName.trim();
    }

    public String getConfirmerSignature() {
        return confirmerSignature;
    }

    public void setConfirmerSignature(String confirmerSignature) {
        this.confirmerSignature = confirmerSignature == null ? null : confirmerSignature.trim();
    }

    public String getPhotoDoor() {
        return photoDoor;
    }

    public void setPhotoDoor(String photoDoor) {
        this.photoDoor = photoDoor == null ? null : photoDoor.trim();
    }

    public String getPhotoBefore1() {
        return photoBefore1;
    }

    public void setPhotoBefore1(String photoBefore1) {
        this.photoBefore1 = photoBefore1 == null ? null : photoBefore1.trim();
    }

    public String getPhotoAfter1() {
        return photoAfter1;
    }

    public void setPhotoAfter1(String photoAfter1) {
        this.photoAfter1 = photoAfter1 == null ? null : photoAfter1.trim();
    }

    public String getPhotoBefore2() {
        return photoBefore2;
    }

    public void setPhotoBefore2(String photoBefore2) {
        this.photoBefore2 = photoBefore2 == null ? null : photoBefore2.trim();
    }

    public String getPhotoAfter2() {
        return photoAfter2;
    }

    public void setPhotoAfter2(String photoAfter2) {
        this.photoAfter2 = photoAfter2 == null ? null : photoAfter2.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Boolean deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    public String getFixReason() {
        return fixReason;
    }

    public void setFixReason(String fixReason) {
        this.fixReason = fixReason == null ? null : fixReason.trim();
    }
}