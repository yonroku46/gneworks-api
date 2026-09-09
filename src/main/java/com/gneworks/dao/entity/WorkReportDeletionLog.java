package com.gneworks.dao.entity;

import jakarta.annotation.Generated;
import java.util.Date;

public class WorkReportDeletionLog {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.log_id, Type: CHAR(27), Remark: 삭제 로그 고유 키 (UUID)")
    private String logId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.report_id, Type: CHAR(27), Remark: 보고서 고유 키")
    private String reportId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.household_id, Type: CHAR(27), Remark: 세대 고유 키")
    private String householdId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.site_id, Type: CHAR(27), Remark: 현장 고유 키")
    private String siteId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.user_id, Type: CHAR(27), Remark: 작성 작업자 고유 키")
    private String userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.dong, Type: VARCHAR(20), Remark: 동 (예: 101동)")
    private String dong;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.ho, Type: VARCHAR(20), Remark: 호수 (예: 101호)")
    private String ho;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.head_name, Type: VARCHAR(50), Remark: 세대주 성명")
    private String headName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.install_date, Type: DATE, Remark: 시공 설치 일자")
    private Date installDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.report_time, Type: DATETIME, Remark: 보고서 제출 일시")
    private Date reportTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.reporter_name, Type: VARCHAR(50), Remark: 보고자 성명")
    private String reporterName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.confirmer_name, Type: VARCHAR(50), Remark: 확인자 성명")
    private String confirmerName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.confirmer_signature, Type: VARCHAR(300), Remark: 서명 이미지 경로 (삭제 전 경로 기록)")
    private String confirmerSignature;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.photo_door, Type: VARCHAR(300), Remark: 현관 사진 경로 (삭제 전 경로 기록)")
    private String photoDoor;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.photo_before1, Type: VARCHAR(300), Remark: 설치전1 사진 경로 (삭제 전 경로 기록)")
    private String photoBefore1;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.photo_after1, Type: VARCHAR(300), Remark: 설치후1 사진 경로 (삭제 전 경로 기록)")
    private String photoAfter1;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.photo_before2, Type: VARCHAR(300), Remark: 설치전2 사진 경로 (삭제 전 경로 기록)")
    private String photoBefore2;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.photo_after2, Type: VARCHAR(300), Remark: 설치후2 사진 경로 (삭제 전 경로 기록)")
    private String photoAfter2;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.status, Type: VARCHAR(20), Remark: 삭제 당시 보고서 상태 (PENDING, COMPLETED, REJECTED)")
    private String status;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.remarks, Type: VARCHAR(500), Remark: 특이사항 비고")
    private String remarks;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.create_time, Type: DATETIME, Remark: 원본 보고서 생성 일시")
    private Date createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.site_name, Type: VARCHAR(100), Remark: 현장명(아파트명)")
    private String siteName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.region_id, Type: VARCHAR(50), Remark: 소방관할 구역 키 (fire_region.region_id)")
    private String regionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.sido, Type: VARCHAR(50), Remark: 시/도")
    private String sido;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.sigungu, Type: VARCHAR(50), Remark: 시/군/구")
    private String sigungu;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.eupmyeondong, Type: VARCHAR(50), Remark: 읍/면/동")
    private String eupmyeondong;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.address, Type: VARCHAR(300), Remark: 현장 상세 주소")
    private String address;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.deleted_by, Type: CHAR(27), Remark: 삭제 실행 관리자 ID (user_id)")
    private String deletedBy;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.deleted_by_name, Type: VARCHAR(50), Remark: 삭제 실행 관리자 성명")
    private String deletedByName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.deleted_time, Type: DATETIME, Default value: CURRENT_TIMESTAMP, Remark: 삭제 처리 일시")
    private Date deletedTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.fix_reason, Type: TEXT, Remark: 기존 반려 사유")
    private String fixReason;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: work_report_deletion_log.delete_reason, Type: TEXT, Remark: 관리자가 입력한 삭제 필수 사유/메모")
    private String deleteReason;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLogId() {
        return logId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getReportId() {
        return reportId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setReportId(String reportId) {
        this.reportId = reportId == null ? null : reportId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getHouseholdId() {
        return householdId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setHouseholdId(String householdId) {
        this.householdId = householdId == null ? null : householdId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getSiteId() {
        return siteId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
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
    public String getDong() {
        return dong;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDong(String dong) {
        this.dong = dong == null ? null : dong.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getHo() {
        return ho;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setHo(String ho) {
        this.ho = ho == null ? null : ho.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getHeadName() {
        return headName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setHeadName(String headName) {
        this.headName = headName == null ? null : headName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getInstallDate() {
        return installDate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getReportTime() {
        return reportTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getReporterName() {
        return reporterName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setReporterName(String reporterName) {
        this.reporterName = reporterName == null ? null : reporterName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getConfirmerName() {
        return confirmerName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setConfirmerName(String confirmerName) {
        this.confirmerName = confirmerName == null ? null : confirmerName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getConfirmerSignature() {
        return confirmerSignature;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setConfirmerSignature(String confirmerSignature) {
        this.confirmerSignature = confirmerSignature == null ? null : confirmerSignature.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPhotoDoor() {
        return photoDoor;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPhotoDoor(String photoDoor) {
        this.photoDoor = photoDoor == null ? null : photoDoor.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPhotoBefore1() {
        return photoBefore1;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPhotoBefore1(String photoBefore1) {
        this.photoBefore1 = photoBefore1 == null ? null : photoBefore1.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPhotoAfter1() {
        return photoAfter1;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPhotoAfter1(String photoAfter1) {
        this.photoAfter1 = photoAfter1 == null ? null : photoAfter1.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPhotoBefore2() {
        return photoBefore2;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPhotoBefore2(String photoBefore2) {
        this.photoBefore2 = photoBefore2 == null ? null : photoBefore2.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPhotoAfter2() {
        return photoAfter2;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPhotoAfter2(String photoAfter2) {
        this.photoAfter2 = photoAfter2 == null ? null : photoAfter2.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getStatus() {
        return status;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getRemarks() {
        return remarks;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
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
    public String getSiteName() {
        return siteName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSiteName(String siteName) {
        this.siteName = siteName == null ? null : siteName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getRegionId() {
        return regionId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getSido() {
        return sido;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSido(String sido) {
        this.sido = sido == null ? null : sido.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getSigungu() {
        return sigungu;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSigungu(String sigungu) {
        this.sigungu = sigungu == null ? null : sigungu.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getEupmyeondong() {
        return eupmyeondong;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setEupmyeondong(String eupmyeondong) {
        this.eupmyeondong = eupmyeondong == null ? null : eupmyeondong.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAddress() {
        return address;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getDeletedBy() {
        return deletedBy;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy == null ? null : deletedBy.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getDeletedByName() {
        return deletedByName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName == null ? null : deletedByName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getDeletedTime() {
        return deletedTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDeletedTime(Date deletedTime) {
        this.deletedTime = deletedTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getFixReason() {
        return fixReason;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setFixReason(String fixReason) {
        this.fixReason = fixReason == null ? null : fixReason.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getDeleteReason() {
        return deleteReason;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason == null ? null : deleteReason.trim();
    }
}