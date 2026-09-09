package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.WorkReportDeletionLog;
import com.gneworks.dto.req.AdminDeletionLogSearchReq;
import jakarta.annotation.Generated;
import java.util.List;

public interface WorkReportDeletionLogMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int deleteByPrimaryKey(String logId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int insert(WorkReportDeletionLog row);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    WorkReportDeletionLog selectByPrimaryKey(String logId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    List<WorkReportDeletionLog> selectAll();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    int updateByPrimaryKey(WorkReportDeletionLog row);

    List<WorkReportDeletionLog> selectDeletionLogs(AdminDeletionLogSearchReq req);

    long selectDeletionLogsCount(AdminDeletionLogSearchReq req);
}