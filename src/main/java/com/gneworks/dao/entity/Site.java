package com.gneworks.dao.entity;

import jakarta.annotation.Generated;
import java.util.Date;

public class Site {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: site.site_id, Type: CHAR(27), Remark: 현장 키")
    private String siteId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: site.region_id, Type: VARCHAR(50), Remark: 지역 키")
    private String regionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: site.name, Type: VARCHAR(100), Remark: 현장명")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: site.region, Type: VARCHAR(100), Remark: 지역")
    private String region;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: site.address, Type: VARCHAR(300), Remark: 주소")
    private String address;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: site.sido, Type: VARCHAR(50), Remark: 시도")
    private String sido;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: site.sigungu, Type: VARCHAR(50), Remark: 시군구")
    private String sigungu;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: site.eupmyeondong, Type: VARCHAR(50), Remark: 읍면동")
    private String eupmyeondong;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: site.contact_phone, Type: VARCHAR(15), Remark: 연락처")
    private String contactPhone;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: site.create_time, Type: DATETIME, Default value: CURRENT_TIMESTAMP, Remark: 생성 일시")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getSiteId() {
        return siteId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
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
    public String getName() {
        return name;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getRegion() {
        return region;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
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
    public String getContactPhone() {
        return contactPhone;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
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