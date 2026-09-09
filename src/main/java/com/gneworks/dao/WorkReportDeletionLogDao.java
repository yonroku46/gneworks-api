package com.gneworks.dao;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.entity.WorkReportDeletionLog;
import com.gneworks.dao.mapper.WorkReportDeletionLogMapper;
import com.gneworks.dto.req.AdminDeletionLogSearchReq;
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
public class WorkReportDeletionLogDao {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private WorkReportDeletionLogMapper workReportDeletionLogMapper;

    public int insert(WorkReportDeletionLog logRecord) {
        try {
            return workReportDeletionLogMapper.insert(logRecord);
        } catch (Exception exception) {
            final String methodName = "WorkReportDeletionLogMapper#insert";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("logRecord", logRecord);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public List<WorkReportDeletionLog> selectDeletionLogs(AdminDeletionLogSearchReq req) {
        try {
            return workReportDeletionLogMapper.selectDeletionLogs(req);
        } catch (Exception exception) {
            final String methodName = "WorkReportDeletionLogMapper#selectDeletionLogs";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("req", req);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public long selectDeletionLogsCount(AdminDeletionLogSearchReq req) {
        try {
            return workReportDeletionLogMapper.selectDeletionLogsCount(req);
        } catch (Exception exception) {
            final String methodName = "WorkReportDeletionLogMapper#selectDeletionLogsCount";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("req", req);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }
}
