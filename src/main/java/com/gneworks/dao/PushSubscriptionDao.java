package com.gneworks.dao;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.entity.PushSubscription;
import com.gneworks.dao.mapper.PushSubscriptionMapper;
import com.gneworks.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class PushSubscriptionDao {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private PushSubscriptionMapper pushSubscriptionMapper;

    public int insert(PushSubscription subscription) {
        try {
            return pushSubscriptionMapper.insert(subscription);
        } catch (Exception exception) {
            final String methodName = "PushSubscriptionMapper#insert";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("subscription", subscription);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int updateByPrimaryKey(PushSubscription subscription) {
        try {
            return pushSubscriptionMapper.updateByPrimaryKey(subscription);
        } catch (Exception exception) {
            final String methodName = "PushSubscriptionMapper#updateByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("subscription", subscription);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public PushSubscription selectByPrimaryKey(String subscriptionId) {
        try {
            return pushSubscriptionMapper.selectByPrimaryKey(subscriptionId);
        } catch (Exception exception) {
            final String methodName = "PushSubscriptionMapper#selectByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("subscriptionId", subscriptionId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public PushSubscription selectByEndpoint(String endpoint) {
        try {
            return pushSubscriptionMapper.selectByEndpoint(endpoint);
        } catch (Exception exception) {
            final String methodName = "PushSubscriptionMapper#selectByEndpoint";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("endpoint", endpoint);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public List<PushSubscription> selectByUserId(String userId) {
        try {
            return pushSubscriptionMapper.selectByUserId(userId);
        } catch (Exception exception) {
            final String methodName = "PushSubscriptionMapper#selectByUserId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public List<PushSubscription> selectAdminSubscriptions() {
        try {
            return pushSubscriptionMapper.selectAdminSubscriptions();
        } catch (Exception exception) {
            final String methodName = "PushSubscriptionMapper#selectAdminSubscriptions";
            Map<String, Object> paramMap = new HashMap<>();
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int deleteByPrimaryKey(String subscriptionId) {
        try {
            return pushSubscriptionMapper.deleteByPrimaryKey(subscriptionId);
        } catch (Exception exception) {
            final String methodName = "PushSubscriptionMapper#deleteByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("subscriptionId", subscriptionId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int deleteByEndpoint(String endpoint) {
        try {
            return pushSubscriptionMapper.deleteByEndpoint(endpoint);
        } catch (Exception exception) {
            final String methodName = "PushSubscriptionMapper#deleteByEndpoint";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("endpoint", endpoint);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int deleteByUserIdAndEndpoint(String userId, String endpoint) {
        try {
            return pushSubscriptionMapper.deleteByUserIdAndEndpoint(userId, endpoint);
        } catch (Exception exception) {
            final String methodName = "PushSubscriptionMapper#deleteByUserIdAndEndpoint";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userId);
            paramMap.put("endpoint", endpoint);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public void saveOrUpdate(PushSubscription subscription) {
        PushSubscription existing = selectByEndpoint(subscription.getEndpoint());
        if (existing != null) {
            existing.setUserId(subscription.getUserId());
            existing.setP256dh(subscription.getP256dh());
            existing.setAuth(subscription.getAuth());
            existing.setUserAgent(subscription.getUserAgent());
            existing.setUpdateTime(new Date());
            updateByPrimaryKey(existing);
        } else {
            insert(subscription);
        }
    }
}