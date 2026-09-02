package com.gneworks.api.service;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.ResponseUtils;
import com.gneworks.dao.AppNotificationDao;
import com.gneworks.dao.entity.AppNotification;
import com.gneworks.dto.res.ActionRes;
import com.gneworks.dto.res.ListRes;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AppNotificationService {

        @Autowired
        MessageSource messageSource;

        @Autowired
        private AppNotificationDao appNotificationDao;

        @Transactional(readOnly = true)
        public BaseResponse getNotifications(String userId) {
                List<AppNotification> list = appNotificationDao.selectByUserId(userId);
                return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_GETTING_SUCCESS,
                                messageSource.getMessage(MessageIdConst.I_GETTING_SUCCESS, new String[] { "AppNotification" }, LocaleAspect.LOCALE)), new ListRes<>(list));
        }

        @Transactional
        public BaseResponse markAsRead(String userId, String notificationId) {
                ActionRes res = new ActionRes();
                AppNotification notification = appNotificationDao.selectByPrimaryKey(notificationId);

                if (notification == null || !notification.getUserId().equals(userId)) {
                        return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.I_UPDATE_FAILED,
                                        messageSource.getMessage(MessageIdConst.I_UPDATE_FAILED, null, LocaleAspect.LOCALE)));
                }

                notification.setIsRead(Boolean.TRUE);
                appNotificationDao.updateByPrimaryKey(notification);
                res.setSuccess(Boolean.TRUE);

                return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_UPDATE_SUCCESS,
                                messageSource.getMessage(MessageIdConst.I_UPDATE_SUCCESS, new String[] { "AppNotification" }, LocaleAspect.LOCALE)), res);
        }

        @Transactional
        public BaseResponse markAllAsRead(String userId) {
                ActionRes res = new ActionRes();
                appNotificationDao.updateAllAsReadByUserId(userId);
                res.setSuccess(Boolean.TRUE);

                return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_UPDATE_SUCCESS,
                                messageSource.getMessage(MessageIdConst.I_UPDATE_SUCCESS, new String[] { "AppNotification" }, LocaleAspect.LOCALE)), res);
        }
}
