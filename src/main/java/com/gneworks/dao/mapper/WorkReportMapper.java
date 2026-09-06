package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.WorkReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

}