package com.gneworks.dao.mapper;

import com.gneworks.dao.entity.Site;
import com.gneworks.dto.res.AdminSiteRes;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SiteMapper {
    int deleteByPrimaryKey(String siteId);

    int insert(Site row);

    Site selectByPrimaryKey(String siteId);

    List<Site> selectAll();

    int updateByPrimaryKey(Site row);

    List<AdminSiteRes> selectSiteList(
        @Param("regionId") String regionId,
        @Param("query") String query,
        @Param("limit") Integer limit,
        @Param("orderBy") String orderBy
    );

    List<AdminSiteRes> selectSiteListPaged(
        @Param("regionId") String regionId,
        @Param("query") String query,
        @Param("offset") int offset,
        @Param("size") int size
    );

    long selectSiteListCount(
        @Param("regionId") String regionId,
        @Param("query") String query
    );

    AdminSiteRes selectSiteDetailWithHouseholds(@Param("siteId") String siteId);

    java.util.Map<String, Object> selectRegionalHouseholdSummary(
        @Param("regionId") String regionId
    );
}
