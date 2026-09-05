package com.gneworks.dao.entity;

import jakarta.annotation.Generated;
import java.util.Date;

public class Household {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: household.household_id, Type: CHAR(27), Remark: 세대 키")
    private String householdId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: household.site_id, Type: CHAR(27), Remark: 현장 키")
    private String siteId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: household.dong, Type: VARCHAR(20), Remark: 동")
    private String dong;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: household.ho, Type: VARCHAR(20), Remark: 호")
    private String ho;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: household.head_name, Type: VARCHAR(50), Remark: 세대주명")
    private String headName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: household.target_type, Type: VARCHAR(20), Remark: 대상 유형")
    private String targetType;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: household.install_status, Type: VARCHAR(20), Remark: 설치 상태")
    private String installStatus;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: household.remarks, Type: VARCHAR(500), Remark: 비고")
    private String remarks;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: household.create_time, Type: DATETIME, Default value: CURRENT_TIMESTAMP, Remark: 생성 일시")
    private Date createTime;

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
    public String getTargetType() {
        return targetType;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTargetType(String targetType) {
        this.targetType = targetType == null ? null : targetType.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getInstallStatus() {
        return installStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setInstallStatus(String installStatus) {
        this.installStatus = installStatus == null ? null : installStatus.trim();
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
}