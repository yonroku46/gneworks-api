package com.gneworks.dao.entity;

import jakarta.annotation.Generated;

public class FireRegion {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: fire_region.region_id, Type: VARCHAR(50), Remark: 소방관할 고유 키")
    private String regionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: fire_region.sido_code, Type: VARCHAR(10), Remark: 시도 표준 코드 (예: 41)")
    private String sidoCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: fire_region.sido_name, Type: VARCHAR(50), Remark: 시도 명칭 (예: 경기도, 서울특별시)")
    private String sidoName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: fire_region.name, Type: VARCHAR(50), Remark: 소방관할서 명칭 (예: 수원, 수원남부, 분당, 화성동탄)")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: fire_region.eupmyeondongs, Type: JSON(0), Remark: 관할 하위 법정동/행정동 목록 JSON 배열")
    private String eupmyeondongs;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getRegionId() {
        return regionId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getSidoCode() {
        return sidoCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSidoCode(String sidoCode) {
        this.sidoCode = sidoCode == null ? null : sidoCode.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getSidoName() {
        return sidoName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSidoName(String sidoName) {
        this.sidoName = sidoName == null ? null : sidoName.trim();
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
    public String getEupmyeondongs() {
        return eupmyeondongs;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setEupmyeondongs(String eupmyeondongs) {
        this.eupmyeondongs = eupmyeondongs == null ? null : eupmyeondongs.trim();
    }
}