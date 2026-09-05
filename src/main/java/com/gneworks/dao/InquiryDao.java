package com.gneworks.dao;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.entity.Inquiry;
import com.gneworks.dao.mapper.InquiryMapper;
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
public class InquiryDao {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private InquiryMapper inquiryMapper;

    public int saveInquiry(Inquiry inquiry) {
        try {
            return inquiryMapper.saveInquiry(inquiry);
        } catch (Exception exception) {
            final String methodName = "InquiryMapper#saveInquiry";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("inquiry", inquiry);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public List<Inquiry> selectAll() {
        try {
            return inquiryMapper.selectAll();
        } catch (Exception exception) {
            final String methodName = "InquiryMapper#selectAll";
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, null, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public Inquiry selectByPrimaryKey(String inquiryId) {
        try {
            return inquiryMapper.selectByPrimaryKey(inquiryId);
        } catch (Exception exception) {
            final String methodName = "InquiryMapper#selectByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("inquiryId", inquiryId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int updateByPrimaryKey(Inquiry inquiry) {
        try {
            return inquiryMapper.updateByPrimaryKey(inquiry);
        } catch (Exception exception) {
            final String methodName = "InquiryMapper#updateByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("inquiry", inquiry);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int deleteByPrimaryKey(String inquiryId) {
        try {
            return inquiryMapper.deleteByPrimaryKey(inquiryId);
        } catch (Exception exception) {
            final String methodName = "InquiryMapper#deleteByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("inquiryId", inquiryId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int countPendingInquiries() {
        try {
            return inquiryMapper.countPendingInquiries();
        } catch (Exception exception) {
            final String methodName = "InquiryMapper#countPendingInquiries";
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, null, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public Inquiry selectLatestPendingInquiry() {
        try {
            return inquiryMapper.selectLatestPendingInquiry();
        } catch (Exception exception) {
            final String methodName = "InquiryMapper#selectLatestPendingInquiry";
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, null, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }
}