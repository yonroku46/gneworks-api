package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.AppNotification;
import jakarta.annotation.Generated;
import java.util.List;

public interface AppNotificationMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(String appNotificationId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(AppNotification row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    AppNotification selectByPrimaryKey(String appNotificationId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<AppNotification> selectAll();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(AppNotification row);

    List<AppNotification> selectByUserId(String userId);

    int updateAllAsReadByUserId(String userId);
}