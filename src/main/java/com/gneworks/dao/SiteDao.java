package com.gneworks.dao;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.entity.FireRegion;
import com.gneworks.dao.entity.Household;
import com.gneworks.dao.entity.Site;
import com.gneworks.dao.entity.UserAssignedRegion;
import com.gneworks.dao.mapper.FireRegionMapper;
import com.gneworks.dao.mapper.HouseholdMapper;
import com.gneworks.dao.mapper.SiteMapper;
import com.gneworks.dao.mapper.UserAssignedRegionMapper;
import com.gneworks.dto.res.AdminSiteRes;
import com.gneworks.dto.res.HouseholdRes;
import com.gneworks.dto.res.RegionWorkerRes;
import com.gneworks.dto.res.UserAssignedRegionDetailRes;
import com.gneworks.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class SiteDao {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private HouseholdMapper householdMapper;

    @Autowired
    private FireRegionMapper fireRegionMapper;

    @Autowired
    private UserAssignedRegionMapper userAssignedRegionMapper;

    // ── [1. Site] ───────────────────────────────────────────

    public List<AdminSiteRes> selectSiteList(String regionId, String query) {
        return selectSiteList(regionId, query, null, null);
    }

    public List<AdminSiteRes> selectSiteList(String regionId, String query, Integer limit, String orderBy) {
        try {
            return siteMapper.selectSiteList(regionId, query, limit, orderBy);
        } catch (Exception exception) {
            final String methodName = "SiteMapper#selectSiteList";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("regionId", regionId);
            paramMap.put("query", query);
            paramMap.put("limit", limit);
            paramMap.put("orderBy", orderBy);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public List<AdminSiteRes> selectSiteListPaged(String regionId, String query, int page, int size) {
        try {
            int offset = Math.max(0, (page - 1) * size);
            return siteMapper.selectSiteListPaged(regionId, query, offset, size);
        } catch (Exception exception) {
            final String methodName = "SiteMapper#selectSiteListPaged";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("regionId", regionId);
            paramMap.put("query", query);
            paramMap.put("page", page);
            paramMap.put("size", size);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public long selectSiteListCount(String regionId, String query) {
        try {
            return siteMapper.selectSiteListCount(regionId, query);
        } catch (Exception exception) {
            final String methodName = "SiteMapper#selectSiteListCount";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("regionId", regionId);
            paramMap.put("query", query);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public AdminSiteRes selectSiteDetailWithHouseholds(String siteId) {
        try {
            return siteMapper.selectSiteDetailWithHouseholds(siteId);
        } catch (Exception exception) {
            final String methodName = "SiteMapper#selectSiteDetailWithHouseholds";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("siteId", siteId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public Site selectSiteById(String siteId) {
        try {
            return siteMapper.selectByPrimaryKey(siteId);
        } catch (Exception exception) {
            final String methodName = "SiteMapper#selectByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("siteId", siteId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int insertSite(Site site) {
        try {
            return siteMapper.insert(site);
        } catch (Exception exception) {
            final String methodName = "SiteMapper#insert";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("site", site);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int updateSite(Site site) {
        try {
            return siteMapper.updateByPrimaryKey(site);
        } catch (Exception exception) {
            final String methodName = "SiteMapper#updateByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("site", site);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int deleteSite(String siteId) {
        try {
            return siteMapper.deleteByPrimaryKey(siteId);
        } catch (Exception exception) {
            final String methodName = "SiteMapper#deleteByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("siteId", siteId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    // ── [2. Household] ──────────────────────────────────────

    public int insertHousehold(Household household) {
        try {
            return householdMapper.insert(household);
        } catch (Exception exception) {
            final String methodName = "HouseholdMapper#insert";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("household", household);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int deleteHousehold(String householdId) {
        try {
            return householdMapper.deleteByPrimaryKey(householdId);
        } catch (Exception exception) {
            final String methodName = "HouseholdMapper#deleteByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("householdId", householdId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int deleteHouseholdsBySiteId(String siteId) {
        try {
            return householdMapper.deleteBySiteId(siteId);
        } catch (Exception exception) {
            final String methodName = "HouseholdMapper#deleteBySiteId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("siteId", siteId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public List<HouseholdRes> selectHouseholdsBySiteId(String siteId) {
        try {
            return householdMapper.selectBySiteId(siteId);
        } catch (Exception exception) {
            final String methodName = "HouseholdMapper#selectBySiteId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("siteId", siteId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public Household selectHouseholdById(String householdId) {
        try {
            return householdMapper.selectByPrimaryKey(householdId);
        } catch (Exception exception) {
            final String methodName = "HouseholdMapper#selectByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("householdId", householdId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int updateHousehold(Household household) {
        try {
            return householdMapper.updateByPrimaryKey(household);
        } catch (Exception exception) {
            final String methodName = "HouseholdMapper#updateByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("household", household);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    // ── [3. FireRegion] ─────────────────────────────────────

    public List<FireRegion> selectAllFireRegions() {
        try {
            return fireRegionMapper.selectAll();
        } catch (Exception exception) {
            final String methodName = "FireRegionMapper#selectAll";
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, new HashMap<>(), exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public FireRegion selectFireRegionBySidoAndName(String sidoName, String name) {
        try {
            return fireRegionMapper.selectBySidoAndName(sidoName, name);
        } catch (Exception exception) {
            final String methodName = "FireRegionMapper#selectBySidoAndName";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("sidoName", sidoName);
            paramMap.put("name", name);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public FireRegion selectFireRegionById(String regionId) {
        try {
            return fireRegionMapper.selectByPrimaryKey(regionId);
        } catch (Exception exception) {
            final String methodName = "FireRegionMapper#selectByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("regionId", regionId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    // ── [4. UserAssignedRegion] ─────────────────────────────

    public List<RegionWorkerRes> selectRegionWorkers(String regionId) {
        try {
            return userAssignedRegionMapper.selectRegionWorkers(regionId);
        } catch (Exception exception) {
            final String methodName = "UserAssignedRegionMapper#selectRegionWorkers";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("regionId", regionId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public List<UserAssignedRegionDetailRes> selectAssignedRegionsByUserId(String userId) {
        try {
            return userAssignedRegionMapper.selectAssignedRegionsByUserId(userId);
        } catch (Exception exception) {
            final String methodName = "UserAssignedRegionMapper#selectAssignedRegionsByUserId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int insertAssignedRegion(UserAssignedRegion uar) {
        try {
            return userAssignedRegionMapper.insert(uar);
        } catch (Exception exception) {
            final String methodName = "UserAssignedRegionMapper#insert";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("uar", uar);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int deleteAssignedRegion(String assignedRegionId) {
        try {
            return userAssignedRegionMapper.deleteByPrimaryKey(assignedRegionId);
        } catch (Exception exception) {
            final String methodName = "UserAssignedRegionMapper#deleteByPrimaryKey";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("assignedRegionId", assignedRegionId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public int deleteAssignedRegionByUserIdAndRegionId(String userId, String regionId) {
        try {
            return userAssignedRegionMapper.deleteByUserIdAndRegionId(userId, regionId);
        } catch (Exception exception) {
            final String methodName = "UserAssignedRegionMapper#deleteByUserIdAndRegionId";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userId);
            paramMap.put("regionId", regionId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }

    public Map<String, Object> selectRegionalHouseholdSummary(String regionId) {
        try {
            return siteMapper.selectRegionalHouseholdSummary(regionId);
        } catch (Exception exception) {
            final String methodName = "SiteMapper#selectRegionalHouseholdSummary";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("regionId", regionId);
            String overview = messageSource.getMessage(MessageIdConst.E_SQL_ISSUE, null, LocaleAspect.LOCALE);
            String detail = StringUtils.convertInterfaceErrorMsg(methodName, paramMap, exception);
            log.error(overview + detail);
            throw new SystemException(MessageIdConst.E_SQL_ISSUE, overview, detail);
        }
    }
}
