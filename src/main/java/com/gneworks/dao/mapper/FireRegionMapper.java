package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.FireRegion;
import jakarta.annotation.Generated;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FireRegionMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(String regionId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(FireRegion row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    FireRegion selectByPrimaryKey(String regionId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<FireRegion> selectAll();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(FireRegion row);

    FireRegion selectBySidoAndName(@Param("sidoName") String sidoName, @Param("name") String name);
}
