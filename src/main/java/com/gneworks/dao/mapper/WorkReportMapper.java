package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.WorkReport;
import com.gneworks.dto.req.AdminReportSearchReq;
import com.gneworks.dto.res.AdminWorkerStatRes;
import com.gneworks.dto.res.WorkReportRes;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WorkReportMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String reportId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(WorkReport row);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    WorkReport selectByPrimaryKey(String reportId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<WorkReport> selectAll();

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(WorkReport row);

    WorkReport selectByHouseholdId(String householdId);

    List<WorkReport> selectBySiteId(String siteId);

    List<WorkReport> selectByUserId(String userId);

    int updateByHouseholdId(WorkReport row);

    List<WorkReportRes> selectReportList(AdminReportSearchReq req);

    WorkReportRes selectReportDetailById(@Param("reportId") String reportId);

    WorkReportRes selectReportDetailByHouseholdId(@Param("householdId") String householdId);

    int updateReportStatus(
            @Param("reportId") String reportId,
            @Param("status") String status,
            @Param("fixReason") String fixReason
    );

    long selectReportCount(AdminReportSearchReq req);

    List<AdminWorkerStatRes> selectWorkerRanking(
            @Param("regionId") String regionId,
            @Param("limit") Integer limit
    );

    long selectWorkerRankingCount(
            @Param("regionId") String regionId
    );

    Map<String, Object> selectReportSummary(
            @Param("regionId") String regionId
    );
}