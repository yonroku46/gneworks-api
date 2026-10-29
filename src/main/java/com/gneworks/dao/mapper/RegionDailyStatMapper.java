package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.RegionDailyStat;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RegionDailyStatMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(@Param("statDate") Date statDate, @Param("regionId") String regionId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(RegionDailyStat row);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    RegionDailyStat selectByPrimaryKey(@Param("statDate") Date statDate, @Param("regionId") String regionId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<RegionDailyStat> selectAll();

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(RegionDailyStat row);

    /**
     * 지정 일자의 소방관할별 일일 통계 UPSERT (배치용)
     */
    int upsertDailyStatsForDate(@Param("statDate") Date statDate);

    /**
     * 일별 소방관할 통계 조회
     */
    List<RegionDailyStat> selectDailyStats(@Param("statDate") Date statDate);

    /**
     * 월별 기간 내 소방관할별 통계 집계 조회
     */
    List<RegionDailyStat> selectMonthlyStats(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 기간 내 집계 레코드 수 확인
     */
    int countByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}