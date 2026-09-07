package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.Household;
import com.gneworks.dto.res.HouseholdRes;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface HouseholdMapper {
    int deleteByPrimaryKey(String householdId);

    int deleteBySiteId(@Param("siteId") String siteId);

    int insert(Household row);

    int insertBatch(@Param("list") List<Household> list);

    Household selectByPrimaryKey(String householdId);

    Household selectBySiteIdAndDongAndHo(@Param("siteId") String siteId, @Param("dong") String dong, @Param("ho") String ho);

    List<Household> selectAll();

    List<HouseholdRes> selectBySiteId(@Param("siteId") String siteId);

    int updateByPrimaryKey(Household row);
}
