package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.Household;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface HouseholdMapper {
    int deleteByPrimaryKey(String householdId);

    int deleteBySiteId(@Param("siteId") String siteId);

    int insert(Household row);

    Household selectByPrimaryKey(String householdId);

    List<Household> selectAll();

    List<Household> selectBySiteId(@Param("siteId") String siteId);

    int updateByPrimaryKey(Household row);
}
