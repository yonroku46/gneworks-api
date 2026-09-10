package com.gneworks.dao;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.entity.UserNotificationSetting;
import com.gneworks.dao.mapper.UserNotificationSettingMapper;
import com.gneworks.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class UserNotificationSettingDao {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserNotificationSettingMapper userNotificationSettingMapper;

    public UserNotificationSetting selectByPrimaryKey(String userId) {
        try {
            return userNotificationSettingMapper.selectByPrimaryKey(userId);
        } catch (Exception exception) {
            final String methodName = "UserNotificationSettingMapper#selectByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int insert(UserNotificationSetting row) {
        try {
            return userNotificationSettingMapper.insert(row);
        } catch (Exception exception) {
            final String methodName = "UserNotificationSettingMapper#insert";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("row", row);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int updateByPrimaryKey(UserNotificationSetting row) {
        try {
            return userNotificationSettingMapper.updateByPrimaryKey(row);
        } catch (Exception exception) {
            final String methodName = "UserNotificationSettingMapper#updateByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("row", row);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public void saveOrUpdate(UserNotificationSetting setting) {
        if (setting == null || setting.getUserId() == null) return;
        setting.setUpdateTime(new Date());
        UserNotificationSetting existing = selectByPrimaryKey(setting.getUserId());
        if (existing == null) {
            insert(setting);
        } else {
            updateByPrimaryKey(setting);
        }
    }
}
