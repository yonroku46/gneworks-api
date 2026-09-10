package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.UserNotificationSetting;
import java.util.List;

public interface UserNotificationSettingMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String userId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(UserNotificationSetting row);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    UserNotificationSetting selectByPrimaryKey(String userId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<UserNotificationSetting> selectAll();

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(UserNotificationSetting row);
}