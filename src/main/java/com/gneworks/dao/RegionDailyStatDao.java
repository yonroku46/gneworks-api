package com.gneworks.dao;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.entity.RegionDailyStat;
import com.gneworks.dao.mapper.RegionDailyStatMapper;
import com.gneworks.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class RegionDailyStatDao {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RegionDailyStatMapper regionDailyStatMapper;

    /**
     * 일일 통계 UPSERT (배치용)
     */
    public int upsertDailyStatsForDate(Date statDate) {
        try {
            return regionDailyStatMapper.upsertDailyStatsForDate(statDate);
        } catch (Exception exception) {
            final String methodName = "RegionDailyStatMapper#upsertDailyStatsForDate";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("statDate", statDate);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail, exception);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    /**
     * 월별 기간 내 소방관할별 통계 집계 조회
     */
    public List<RegionDailyStat> selectMonthlyStats(Date startDate, Date endDate) {
        try {
            List<RegionDailyStat> list = regionDailyStatMapper.selectMonthlyStats(startDate, endDate);
            return list != null ? list : Collections.emptyList();
        } catch (Exception exception) {
            final String methodName = "RegionDailyStatMapper#selectMonthlyStats";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("startDate", startDate);
            paramMap.put("endDate", endDate);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail, exception);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    /**
     * 기간 내 데이터 존재 여부 확인
     */
    public int countByDateRange(Date startDate, Date endDate) {
        try {
            return regionDailyStatMapper.countByDateRange(startDate, endDate);
        } catch (Exception exception) {
            final String methodName = "RegionDailyStatMapper#countByDateRange";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("startDate", startDate);
            paramMap.put("endDate", endDate);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail, exception);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }
}
