package com.gneworks.dao;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.entity.WorkReport;
import com.gneworks.dao.mapper.WorkReportMapper;
import com.gneworks.dto.req.AdminReportSearchReq;
import com.gneworks.dto.res.WorkReportRes;
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
public class WorkReportDao {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private WorkReportMapper workReportMapper;

    public int insert(WorkReport workReport) {
        try {
            return workReportMapper.insert(workReport);
        } catch (Exception exception) {
            final String methodName = "WorkReportMapper#insert";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("workReport", workReport);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int updateByPrimaryKey(WorkReport workReport) {
        try {
            return workReportMapper.updateByPrimaryKey(workReport);
        } catch (Exception exception) {
            final String methodName = "WorkReportMapper#updateByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("workReport", workReport);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public WorkReport selectByHouseholdId(String householdId) {
        try {
            return workReportMapper.selectByHouseholdId(householdId);
        } catch (Exception exception) {
            final String methodName = "WorkReportMapper#selectByHouseholdId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("householdId", householdId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public List<WorkReport> selectBySiteId(String siteId) {
        try {
            return workReportMapper.selectBySiteId(siteId);
        } catch (Exception exception) {
            final String methodName = "WorkReportMapper#selectBySiteId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("siteId", siteId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public List<WorkReport> selectByUserId(String userId) {
        try {
            return workReportMapper.selectByUserId(userId);
        } catch (Exception exception) {
            final String methodName = "WorkReportMapper#selectByUserId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public List<WorkReportRes> selectReportList(AdminReportSearchReq req) {
        try {
            return workReportMapper.selectReportList(req);
        } catch (Exception exception) {
            final String methodName = "WorkReportMapper#selectReportList";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("req", req);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public WorkReportRes selectReportDetailById(String reportId) {
        try {
            return workReportMapper.selectReportDetailById(reportId);
        } catch (Exception exception) {
            final String methodName = "WorkReportMapper#selectReportDetailById";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("reportId", reportId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public WorkReportRes selectReportDetailByHouseholdId(String householdId) {
        try {
            return workReportMapper.selectReportDetailByHouseholdId(householdId);
        } catch (Exception exception) {
            final String methodName = "WorkReportMapper#selectReportDetailByHouseholdId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("householdId", householdId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int updateReportStatus(String reportId, String status, String fixReason) {
        try {
            return workReportMapper.updateReportStatus(reportId, status, fixReason);
        } catch (Exception exception) {
            final String methodName = "WorkReportMapper#updateReportStatus";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("reportId", reportId);
            paramMap.put("status", status);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }
}