package com.gneworks.dao;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.entity.AppNotification;
import com.gneworks.dao.mapper.AppNotificationMapper;
import com.gneworks.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class AppNotificationDao {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private AppNotificationMapper appNotificationMapper;

    public List<AppNotification> selectByUserId(String userId) {
        try {
            return appNotificationMapper.selectByUserId(userId);
        } catch (Exception exception) {
            final String methodName = "AppNotificationMapper#selectByUserId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int updateByPrimaryKey(AppNotification notification) {
        try {
            return appNotificationMapper.updateByPrimaryKey(notification);
        } catch (Exception exception) {
            final String methodName = "AppNotificationMapper#updateByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("notification", notification);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int updateAllAsReadByUserId(String userId) {
        try {
            return appNotificationMapper.updateAllAsReadByUserId(userId);
        } catch (Exception exception) {
            final String methodName = "AppNotificationMapper#updateAllAsReadByUserId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public AppNotification selectByPrimaryKey(String appNotificationId) {
        try {
            return appNotificationMapper.selectByPrimaryKey(appNotificationId);
        } catch (Exception exception) {
            final String methodName = "AppNotificationMapper#selectByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("appNotificationId", appNotificationId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }
}
