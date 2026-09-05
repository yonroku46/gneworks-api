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
        @Param("sido") String sido,
        @Param("sigungu") String sigungu,
        @Param("eupmyeondong") String eupmyeondong,
        @Param("query") String query
    );

    AdminSiteRes selectSiteDetailWithHouseholds(@Param("siteId") String siteId);
}
