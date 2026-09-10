package com.gneworks.dao;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.entity.SystemSettings;
import com.gneworks.dao.mapper.SystemSettingsMapper;
import com.gneworks.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class SystemSettingsDao {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SystemSettingsMapper systemSettingsMapper;

    public SystemSettings selectByPrimaryKey(Byte settingId) {
        try {
            return systemSettingsMapper.selectByPrimaryKey(settingId);
        } catch (Exception exception) {
            final String methodName = "SystemSettingsMapper#selectByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("settingId", settingId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int insert(SystemSettings row) {
        try {
            return systemSettingsMapper.insert(row);
        } catch (Exception exception) {
            final String methodName = "SystemSettingsMapper#insert";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("row", row);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int updateByPrimaryKey(SystemSettings row) {
        try {
            return systemSettingsMapper.updateByPrimaryKey(row);
        } catch (Exception exception) {
            final String methodName = "SystemSettingsMapper#updateByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("row", row);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }
}