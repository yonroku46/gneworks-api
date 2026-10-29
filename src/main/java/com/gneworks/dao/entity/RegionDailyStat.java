package com.gneworks.dao.entity;

import java.util.Date;

/**
 * Table: region_daily_stat
 */
public class RegionDailyStat {
    /**
     * Column: stat_date
     * Type: DATE
     * Remark: 기준일자 (YYYY-MM-DD)
     */
    private Date statDate;

    /**
     * Column: region_id
     * Type: VARCHAR(50)
     * Remark: 소방관할 ID
     */
    private String regionId;

    /**
     * Column: sido_name
     * Type: VARCHAR(50)
     * Remark: 시/도명 (예: 경기도, 강원도)
     */
    private String sidoName;

    /**
     * Column: name
     * Type: VARCHAR(50)
     * Remark: 소방관할서 명칭 (예: 수원, 수원남부, 분당, 화성동탄)
     */
    private String name;

    /**
     * Column: total_sites
     * Type: INT
     * Default value: 0
     * Remark: 관리 대상 현장(단지) 수
     */
    private Integer totalSites;

    /**
     * Column: total_target
     * Type: INT
     * Default value: 0
     * Remark: 관리 대상 총 세대 수
     */
    private Integer totalTarget;

    /**
     * Column: completed_target
     * Type: INT
     * Default value: 0
     * Remark: 설치 완료 세대 수 (당일 실적)
     */
    private Integer completedTarget;

    /**
     * Column: cumulative_completed_target
     * Type: INT
     * Default value: 0
     * Remark: 기준일 시점 누적 설치 완료 세대 수
     */
    private Integer cumulativeCompletedTarget;

    /**
     * Column: total_reports
     * Type: INT
     * Default value: 0
     * Remark: 누적/당일 제출 확인서 건수
     */
    private Integer totalReports;

    /**
     * Column: completed_reports
     * Type: INT
     * Default value: 0
     * Remark: 승인 완료 확인서 건수
     */
    private Integer completedReports;

    /**
     * Column: pending_reports
     * Type: INT
     * Default value: 0
     * Remark: 검토 대기 확인서 건수
     */
    private Integer pendingReports;

    /**
     * Column: refused_reports
     * Type: INT
     * Default value: 0
     * Remark: 설치 거부 건수
     */
    private Integer refusedReports;

    /**
     * Column: issue_reports_count
     * Type: INT
     * Default value: 0
     * Remark: 특이사항 확인서 건수
     */
    private Integer issueReportsCount;

    /**
     * Column: create_time
     * Type: DATETIME
     * Default value: CURRENT_TIMESTAMP
     * Remark: 생성일시
     */
    private Date createTime;

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getSidoName() {
        return sidoName;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName == null ? null : sidoName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getTotalSites() {
        return totalSites;
    }

    public void setTotalSites(Integer totalSites) {
        this.totalSites = totalSites;
    }

    public Integer getTotalTarget() {
        return totalTarget;
    }

    public void setTotalTarget(Integer totalTarget) {
        this.totalTarget = totalTarget;
    }

    public Integer getCompletedTarget() {
        return completedTarget;
    }

    public void setCompletedTarget(Integer completedTarget) {
        this.completedTarget = completedTarget;
    }

    public Integer getCumulativeCompletedTarget() {
        return cumulativeCompletedTarget;
    }

    public void setCumulativeCompletedTarget(Integer cumulativeCompletedTarget) {
        this.cumulativeCompletedTarget = cumulativeCompletedTarget;
    }

    public Integer getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(Integer totalReports) {
        this.totalReports = totalReports;
    }

    public Integer getCompletedReports() {
        return completedReports;
    }

    public void setCompletedReports(Integer completedReports) {
        this.completedReports = completedReports;
    }

    public Integer getPendingReports() {
        return pendingReports;
    }

    public void setPendingReports(Integer pendingReports) {
        this.pendingReports = pendingReports;
    }

    public Integer getRefusedReports() {
        return refusedReports;
    }

    public void setRefusedReports(Integer refusedReports) {
        this.refusedReports = refusedReports;
    }

    public Integer getIssueReportsCount() {
        return issueReportsCount;
    }

    public void setIssueReportsCount(Integer issueReportsCount) {
        this.issueReportsCount = issueReportsCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}