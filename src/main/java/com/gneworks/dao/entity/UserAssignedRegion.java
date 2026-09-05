package com.gneworks.dao.entity;

import jakarta.annotation.Generated;
import java.util.Date;

public class UserAssignedRegion {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user_assigned_region.assigned_region_id, Type: VARCHAR(50), Remark: 참가 관할지역 키")
    private String assignedRegionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user_assigned_region.user_id, Type: CHAR(27), Remark: 사용자 키")
    private String userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user_assigned_region.region_id, Type: VARCHAR(100), Remark: 리전 키")
    private String regionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user_assigned_region.create_time, Type: DATETIME, Default value: CURRENT_TIMESTAMP, Remark: 생성 일시")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAssignedRegionId() {
        return assignedRegionId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAssignedRegionId(String assignedRegionId) {
        this.assignedRegionId = assignedRegionId == null ? null : assignedRegionId.trim();
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
    public String getRegionId() {
        return regionId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
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