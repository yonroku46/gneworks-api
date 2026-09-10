package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.SystemSettings;
import java.util.List;

public interface SystemSettingsMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(Byte settingId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(SystemSettings row);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    SystemSettings selectByPrimaryKey(Byte settingId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<SystemSettings> selectAll();

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(SystemSettings row);
}