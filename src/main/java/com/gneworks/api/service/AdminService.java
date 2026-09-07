package com.gneworks.api.service;

import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.enums.Roles;
import com.gneworks.common.id.KsuidGenerator;
import com.gneworks.common.utils.ResponseUtils;
import com.gneworks.dao.InquiryDao;
import com.gneworks.dao.SiteDao;
import com.gneworks.dao.UserDao;
import com.gneworks.dao.WorkReportDao;
import com.gneworks.dao.entity.FireRegion;
import com.gneworks.dao.entity.Household;
import com.gneworks.dao.entity.Inquiry;
import com.gneworks.dao.entity.Site;
import com.gneworks.dao.entity.User;
import com.gneworks.dao.entity.UserAssignedRegion;
import com.gneworks.dto.req.AdminHouseholdReq;
import com.gneworks.dto.req.AdminInquiryAnswerReq;
import com.gneworks.dto.req.AdminInquirySearchReq;
import com.gneworks.dto.req.AdminReportSearchReq;
import com.gneworks.dto.req.AdminReportStatusReq;
import com.gneworks.dto.req.AdminSiteReq;
import com.gneworks.dto.req.AdminUserReq;
import com.gneworks.dto.req.AdminUserSearchReq;
import com.gneworks.dto.res.ActionRes;
import com.gneworks.dto.res.AdminInquiryRes;
import com.gneworks.dto.res.AdminDashboardSummaryRes;
import com.gneworks.dto.res.AdminInquirySummaryRes;
import com.gneworks.dto.res.AdminSiteRes;
import com.gneworks.dto.res.AdminUserRes;
import com.gneworks.dto.res.AdminWorkerStatRes;
import com.gneworks.dto.res.ListRes;
import com.gneworks.dto.res.PageRes;
import com.gneworks.dto.res.RegionWorkerRes;
import com.gneworks.dto.res.UserAssignedRegionDetailRes;
import com.gneworks.dto.res.WorkReportRes;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import com.gneworks.common.utils.ExcelStreamingUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AdminService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat BIRTH_PW_FORMAT = new SimpleDateFormat("yyMMdd");
    private static final Information INFO_SUCCESS = new Information("SUCCESS", "SUCCESS");

    @Autowired
    private UserDao userDao;

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private InquiryDao inquiryDao;

    @Autowired
    private WorkReportDao workReportDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 관리자(ROOT, Roles.ROOT, role_id = 9) 여부 확인
     */
    private boolean isNotAdmin(String operatorUserId) {
        if (operatorUserId == null || operatorUserId.trim().isEmpty()) {
            return true;
        }
        User operator = userDao.findUser(operatorUserId.trim());
        return operator == null || operator.getRoleId() == null || operator.getRoleId() != Roles.ROOT.getValue();
    }

    // ── [1. 계정 / 작업자 관리] ────────────────────────────────────

    /**
     * 작업자 목록 조회(관리자 제외, role_id = 9 제외)
     */
    @Transactional(readOnly = true)
    public BaseResponse getUserList(String operatorUserId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "FORBIDDEN"));
        }

        List<User> userList = userDao.selectAllActiveUsers();
        List<AdminUserRes> resList = new ArrayList<>();

        for (User u : userList) {
            AdminUserRes res = new AdminUserRes();
            res.setUserId(u.getUserId());
            res.setUserName(u.getUserName());
            res.setPhoneNum(u.getPhoneNum());
            res.setProfileImg(u.getProfileImg());
            res.setRoleId(u.getRoleId());
            res.setGender(u.getGender());
            res.setPostalCode(u.getPostalCode());
            res.setDetailAddress(u.getDetailAddress());

            if (u.getBirthday() != null) {
                res.setBirthday(DATE_FORMAT.format(u.getBirthday()));
            }
            if (u.getLastUpdate() != null) {
                res.setLastUpdated(DATETIME_FORMAT.format(u.getLastUpdate()));
                res.setCreateTime(DATETIME_FORMAT.format(u.getLastUpdate()));
            }
            resList.add(res);
        }

        ListRes<AdminUserRes> listRes = new ListRes<>(resList, resList.size());
        return ResponseUtils.generateDtoSuccess(
                new Information(MessageIdConst.I_GETTING_SUCCESS, "SUCCESS"),
                listRes
        );
    }

    /**
     * 계정 목록 페이징 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getUserListPaged(String operatorUserId, AdminUserSearchReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (req == null) req = new AdminUserSearchReq();
        long totalCount = userDao.selectUserCount(req);
        List<AdminUserRes> users = totalCount > 0
                ? userDao.selectUserListPaged(req)
                : Collections.emptyList();
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new PageRes<>(users, totalCount, req.getPage(), req.getSize()));
    }

    /**
     * 계정 목록 대용량 엑셀 스트리밍 다운로드
     */
    @Transactional(readOnly = true)
    public void exportUsersExcel(String operatorUserId, AdminUserSearchReq req, HttpServletResponse response) throws IOException {
        if (isNotAdmin(operatorUserId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "ACCESS_DENIED");
            return;
        }
        if (req == null) req = new AdminUserSearchReq();
        req.setSize(null);
        List<AdminUserRes> users = userDao.selectUserListPaged(req);
        ExcelStreamingUtil.export(response, "계정목록", users, AdminUserRes.class);
    }

    /**
     * 신규 작업자 계정 생성 (기본 아이디: 하이픈 없는 휴대폰번호, 초기 비밀번호: 생년월일 6자리 yyMMdd)
     */
    @Transactional
    public BaseResponse createUser(String operatorUserId, AdminUserReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "FORBIDDEN"));
        }

        if (req.getUserName() == null || req.getUserName().trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER_NAME", "USER_NAME_REQUIRED"));
        }
        if (req.getPhoneNum() == null || req.getPhoneNum().trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PHONE_NUM", "PHONE_NUM_REQUIRED"));
        }

        // 아이디: 입력된 값이 없으면 하이픈 없는 전화번호로 자동 설정
        String targetUserId = req.getUserId();
        if (targetUserId == null || targetUserId.trim().isEmpty()) {
            targetUserId = req.getPhoneNum().replaceAll("[^0-9]", "");
        } else {
            targetUserId = targetUserId.trim();
        }

        if (targetUserId.isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER_ID", "USER_ID_REQUIRED"));
        }

        User exist = userDao.findUser(targetUserId);
        if (exist != null) {
            return ResponseUtils.generateDtoFailed(new Information("DUPLICATE_USER_ID", "ALREADY_EXISTS"));
        }

        User newUser = new User();
        newUser.setUserId(targetUserId);
        newUser.setUserName(req.getUserName().trim());
        newUser.setPhoneNum(formatPhoneNumber(req.getPhoneNum()));
        newUser.setRoleId(Roles.CLIENT.getValue()); // 작업자 권한(1) 부여
        newUser.setGender(req.getGender());
        newUser.setPostalCode(req.getPostalCode());
        newUser.setDetailAddress(req.getDetailAddress());
        newUser.setDeleteFlg((byte) 0);
        newUser.setLastUpdate(new Date());

        if (req.getBirthday() == null || req.getBirthday().trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_BIRTHDAY", "BIRTHDAY_REQUIRED"));
        }

        try {
            newUser.setBirthday(DATE_FORMAT.parse(req.getBirthday().trim()));
        } catch (Exception e) {
            log.warn("Invalid birthday format: {}", req.getBirthday());
            return ResponseUtils.generateDtoFailed(new Information("INVALID_BIRTHDAY", "INVALID_BIRTHDAY_FORMAT"));
        }

        // 초기 비밀번호 세팅: 생년월일 6자리(yyMMdd) 필수 (전화번호 대체 불가)
        String rawPw = BIRTH_PW_FORMAT.format(newUser.getBirthday());
        newUser.setUserPw(passwordEncoder.encode(rawPw));

        userDao.insertUser(newUser);

        ActionRes res = new ActionRes(newUser.getUserId());
        return ResponseUtils.generateDtoSuccess(
                new Information(MessageIdConst.I_INSERT_SUCCESS, "SUCCESS"),
                res
        );
    }

    /**
     * 계정 기본정보 수정
     */
    @Transactional
    public BaseResponse updateUser(String operatorUserId, AdminUserReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "FORBIDDEN"));
        }

        if (req.getUserId() == null || req.getUserId().trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER_ID", "USER_ID_REQUIRED"));
        }

        User user = userDao.findUser(req.getUserId().trim());
        if (user == null) {
            return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.E_USER_NOT_FOUND, "USER_NOT_FOUND"));
        }

        if (req.getUserName() != null) user.setUserName(req.getUserName().trim());
        if (req.getPhoneNum() != null) user.setPhoneNum(formatPhoneNumber(req.getPhoneNum()));
        if (req.getGender() != null) user.setGender(req.getGender());
        if (req.getPostalCode() != null) user.setPostalCode(req.getPostalCode());
        if (req.getDetailAddress() != null) user.setDetailAddress(req.getDetailAddress());

        if (req.getBirthday() != null && !req.getBirthday().trim().isEmpty()) {
            try {
                user.setBirthday(DATE_FORMAT.parse(req.getBirthday().trim()));
            } catch (Exception e) {
                log.warn("Invalid birthday format: {}", req.getBirthday());
                return ResponseUtils.generateDtoFailed(new Information("INVALID_BIRTHDAY", "INVALID_BIRTHDAY_FORMAT"));
            }
        }

        userDao.updateUserByAdmin(user);

        ActionRes res = new ActionRes(user.getUserId());
        return ResponseUtils.generateDtoSuccess(
                new Information(MessageIdConst.I_UPDATE_SUCCESS, "SUCCESS"),
                res
        );
    }

    /**
     * 비밀번호 초기화 (생년월일 6자리 yyMMdd 필수)
     */
    @Transactional
    public BaseResponse resetPassword(String operatorUserId, String targetUserId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "FORBIDDEN"));
        }

        if (targetUserId == null || targetUserId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER_ID", "USER_ID_REQUIRED"));
        }

        User user = userDao.findUser(targetUserId.trim());
        if (user == null) {
            return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.E_USER_NOT_FOUND, "USER_NOT_FOUND"));
        }

        // 비밀번호 초기화: 생년월일 6자리(yyMMdd) 필수 (전화번호로 대체 불가)
        if (user.getBirthday() == null) {
            return ResponseUtils.generateDtoFailed(new Information("CANNOT_RESET_PASSWORD", "BIRTHDAY_REQUIRED_FOR_RESET"));
        }

        String rawPw = BIRTH_PW_FORMAT.format(user.getBirthday());
        String encodedPw = passwordEncoder.encode(rawPw);
        userDao.updatePassword(user.getUserId(), encodedPw);

        ActionRes res = new ActionRes(user.getUserId());
        return ResponseUtils.generateDtoSuccess(
                new Information(MessageIdConst.I_UPDATE_SUCCESS, "SUCCESS"),
                res
        );
    }

    /**
     * 계정 비활성화(소프트 삭제, delete_flg = 1)
     */
    @Transactional
    public BaseResponse deleteUser(String operatorUserId, String targetUserId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "FORBIDDEN"));
        }

        if (targetUserId == null || targetUserId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER_ID", "USER_ID_REQUIRED"));
        }

        User user = userDao.findUser(targetUserId.trim());
        if (user == null) {
            return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.E_USER_NOT_FOUND, "USER_NOT_FOUND"));
        }

        userDao.softDeleteUser(user.getUserId());

        ActionRes res = new ActionRes(user.getUserId());
        return ResponseUtils.generateDtoSuccess(
                new Information(MessageIdConst.I_DELETE_SUCCESS, "SUCCESS"),
                res
        );
    }

    // ── [2. 현장 / 세대 관리] ────────────────────────────────────


    /**
     * 현장 목록 조회 (대시보드 등 연동용: regionId, query, limit, orderBy 지원)
     */
    @Transactional(readOnly = true)
    public BaseResponse getSiteList(String operatorUserId, String regionId, String query, Integer limit, String orderBy) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        List<AdminSiteRes> sites = siteDao.selectSiteList(regionId, query, limit, orderBy);
        long totalCount = limit != null
                ? siteDao.selectSiteListCount(regionId, query)
                : (sites != null ? sites.size() : 0);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(sites, (int) totalCount));
    }

    /**
     * 현장 목록 페이징 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getSiteListPaged(String operatorUserId, String regionId, String query, int page, int size) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (page < 1) page = 1;
        if (size < 1) size = 30;
        if (size > 100) size = 100;

        long totalCount = siteDao.selectSiteListCount(regionId, query);
        List<AdminSiteRes> sites = totalCount > 0
                ? siteDao.selectSiteListPaged(regionId, query, page, size)
                : Collections.emptyList();

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new PageRes<>(sites, totalCount, page, size));
    }

    /**
     * 현장 목록 대용량 엑셀 스트리밍 다운로드 (제네릭 ExcelStreamingUtil 활용)
     */
    @Transactional(readOnly = true)
    public void exportSitesExcel(String operatorUserId, String regionId, String query, HttpServletResponse response) throws IOException {
        if (isNotAdmin(operatorUserId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "ACCESS_DENIED");
            return;
        }

        List<AdminSiteRes> sites = siteDao.selectSiteList(regionId, query, null, null);
        ExcelStreamingUtil.export(response, "현장목록", sites, AdminSiteRes.class);
    }

    /**
     * 현장 상세 조회(관리자 전용, 작업자 배정 정보 포함)
     */
    @Transactional(readOnly = true)
    public BaseResponse getSiteDetail(String operatorUserId, String siteId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        AdminSiteRes detail = siteDao.selectSiteDetailWithHouseholds(siteId);
        if (detail == null) {
            return ResponseUtils.generateDtoFailed(new Information("SITE_NOT_FOUND", "SITE_NOT_FOUND"));
        }
        if (detail.getHouseholds() == null) {
            detail.setHouseholds(new ArrayList<>());
        } else {
            detail.getHouseholds().removeIf(h -> h == null || h.getHouseholdId() == null);
        }
        detail.setTotalHouseholds(detail.getHouseholds().size());
        detail.setDongCount(new HashSet<>(detail.getHouseholds().stream().map(Household::getDong).filter(Objects::nonNull).toList()).size());
        // regionId(외래키)를 기반으로 해당 소방관할에 배정된 작업자 목록 직접 조회
        List<RegionWorkerRes> workers = null;
        if (detail.getRegionId() != null && !detail.getRegionId().trim().isEmpty()) {
            workers = siteDao.selectRegionWorkers(detail.getRegionId().trim());
        }
        if (workers == null) {
            workers = new ArrayList<>();
        } else {
            workers.removeIf(w -> w == null || w.getUserId() == null);
            for (RegionWorkerRes w : workers) {
                if (w.getAssignedRegions() == null) {
                    w.setAssignedRegions(new ArrayList<>());
                } else {
                    w.getAssignedRegions().removeIf(r -> r == null || r.getAssignedRegionId() == null);
                }
            }
        }
        detail.setAssignedWorkers(workers);

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, detail);
    }

    /**
     * 현장 등록
     */
    @Transactional
    public BaseResponse createSite(String operatorUserId, AdminSiteReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (req.getName() == null || req.getName().trim().isEmpty() ||
            req.getAddress() == null || req.getAddress().trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PARAMETER", "INVALID_PARAMETER"));
        }

        Site site = new Site();
        site.setSiteId(KsuidGenerator.createId());
        site.setName(req.getName().trim());
        site.setAddress(req.getAddress().trim());
        site.setSido(req.getSido() != null ? req.getSido().trim() : "");
        site.setSigungu(req.getSigungu() != null ? req.getSigungu().trim() : "");
        site.setEupmyeondong(req.getEupmyeondong() != null ? req.getEupmyeondong().trim() : "");
        site.setRegion(req.getRegion() != null && !req.getRegion().trim().isEmpty() ? req.getRegion().trim() : site.getSigungu());
        site.setRegionId(req.getRegionId() != null && !req.getRegionId().trim().isEmpty() ? req.getRegionId().trim() : null);
        site.setContactPhone(req.getContactPhone() != null ? req.getContactPhone().trim() : null);
        site.setCreateTime(new Date());

        siteDao.insertSite(site);

        ActionRes res = new ActionRes(site.getSiteId());
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 현장 정보 수정
     */
    @Transactional
    public BaseResponse updateSite(String operatorUserId, String siteId, AdminSiteReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        Site existing = siteDao.selectSiteById(siteId);
        if (existing == null) {
            return ResponseUtils.generateDtoFailed(new Information("SITE_NOT_FOUND", "SITE_NOT_FOUND"));
        }

        if (req.getName() != null && !req.getName().trim().isEmpty()) {
            existing.setName(req.getName().trim());
        }
        if (req.getAddress() != null && !req.getAddress().trim().isEmpty()) {
            existing.setAddress(req.getAddress().trim());
        }
        if (req.getContactPhone() != null) {
            existing.setContactPhone(req.getContactPhone().trim());
        }
        if (req.getSido() != null) existing.setSido(req.getSido().trim());
        if (req.getSigungu() != null) existing.setSigungu(req.getSigungu().trim());
        if (req.getEupmyeondong() != null) existing.setEupmyeondong(req.getEupmyeondong().trim());
        if (req.getRegion() != null) existing.setRegion(req.getRegion().trim());
        if (req.getRegionId() != null) existing.setRegionId(req.getRegionId().trim());

        siteDao.updateSite(existing);

        ActionRes res = new ActionRes(siteId);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 현장 삭제(연관 세대 일괄 삭제)
     */
    @Transactional
    public BaseResponse deleteSite(String operatorUserId, String siteId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        Site existing = siteDao.selectSiteById(siteId);
        if (existing == null) {
            return ResponseUtils.generateDtoFailed(new Information("SITE_NOT_FOUND", "SITE_NOT_FOUND"));
        }

        siteDao.deleteHouseholdsBySiteId(siteId);
        siteDao.deleteSite(siteId);

        ActionRes res = new ActionRes(siteId);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 세대 등록
     */
    @Transactional
    public BaseResponse addHousehold(String operatorUserId, String siteId, AdminHouseholdReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (req.getDong() == null || req.getDong().trim().isEmpty() ||
            req.getHo() == null || req.getHo().trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PARAMETER", "INVALID_PARAMETER"));
        }

        Household household = new Household();
        household.setHouseholdId(KsuidGenerator.createId());
        household.setSiteId(siteId);
        household.setDong(req.getDong().trim());
        household.setHo(req.getHo().trim());
        household.setHeadName(req.getHeadName() != null && !req.getHeadName().trim().isEmpty() ? req.getHeadName().trim() : "미정");
        household.setTargetType(req.getTargetType() != null && !req.getTargetType().trim().isEmpty() ? req.getTargetType().trim() : "GENERAL");
        household.setInstallStatus(req.getInstallStatus() != null && !req.getInstallStatus().trim().isEmpty() ? req.getInstallStatus().trim() : "UNINSTALLED");
        household.setRemarks(req.getRemarks() != null ? req.getRemarks().trim() : null);
        household.setCreateTime(new Date());

        siteDao.insertHousehold(household);

        ActionRes res = new ActionRes(household.getHouseholdId());
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 세대 삭제
     */
    @Transactional
    public BaseResponse deleteHousehold(String operatorUserId, String siteId, String householdId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        siteDao.deleteHousehold(householdId);

        ActionRes res = new ActionRes(householdId);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    // ── [3. 소방관할 및 관할배정] ──────────────────────────────────

    /**
     * 전체 소방관할 목록 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getFireRegions(String operatorUserId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        List<FireRegion> list = siteDao.selectAllFireRegions();
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(list));
    }

    /**
     * 특정 지역 기반 담당 작업자 목록 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getRegionWorkers(String operatorUserId, String regionId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        List<RegionWorkerRes> workers = null;
        if (regionId != null && !regionId.trim().isEmpty()) {
            workers = siteDao.selectRegionWorkers(regionId.trim());
        }
        if (workers == null) {
            workers = new ArrayList<>();
        } else {
            workers.removeIf(w -> w == null || w.getUserId() == null);
            for (RegionWorkerRes w : workers) {
                if (w.getAssignedRegions() == null) {
                    w.setAssignedRegions(new ArrayList<>());
                } else {
                    w.getAssignedRegions().removeIf(r -> r == null || r.getAssignedRegionId() == null);
                }
            }
        }
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(workers));
    }

    /**
     * 특정 작업자의 배정 관할 목록 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getUserAssignedRegions(String operatorUserId, String targetUserId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        List<UserAssignedRegionDetailRes> list = siteDao.selectAssignedRegionsByUserId(targetUserId);
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.removeIf(r -> r == null || r.getAssignedRegionId() == null);
        }
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(list));
    }

    /**
     * 작업자에게 소방관할 배정
     */
    @Transactional
    public BaseResponse assignRegion(String operatorUserId, String targetUserId, String sidoName, String regionName) {
        return assignRegion(operatorUserId, targetUserId, null, sidoName, regionName);
    }

    @Transactional
    public BaseResponse assignRegion(String operatorUserId, String targetUserId, String regionId, String sidoName, String regionName) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (targetUserId == null || targetUserId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER_ID", "INVALID_USER_ID"));
        }

        FireRegion fireRegion = null;
        // 1순위: regionId 고유 식별자 직접 조회
        if (regionId != null && !regionId.trim().isEmpty()) {
            fireRegion = siteDao.selectFireRegionById(regionId.trim());
        }
        // 2순위: 시도 및 소방관할서 명칭 완전 일치 조회
        if (fireRegion == null && sidoName != null && regionName != null) {
            fireRegion = siteDao.selectFireRegionBySidoAndName(sidoName.trim(), regionName.trim());
        }
        if (fireRegion == null) {
            return ResponseUtils.generateDtoFailed(new Information("NOT_FOUND", "NOT_FOUND " + sidoName + " " + regionName));
        }
        UserAssignedRegion uar = new UserAssignedRegion();
        uar.setAssignedRegionId(KsuidGenerator.createId());
        uar.setUserId(targetUserId);
        uar.setRegionId(fireRegion.getRegionId());
        uar.setCreateTime(new Date());

        siteDao.insertAssignedRegion(uar);

        ActionRes res = new ActionRes(uar.getAssignedRegionId());
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 작업자 소방관할 배정 해제
     */
    @Transactional
    public BaseResponse unassignRegion(String operatorUserId, String targetUserId, String regionId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        siteDao.deleteAssignedRegionByUserIdAndRegionId(targetUserId, regionId);

        ActionRes res = new ActionRes(regionId);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    // ── [4. 문의 관리] ──────────────────────────────────────────

    /**
     * 문의 목록 전체 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getInquiryList(String operatorUserId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }

        List<Inquiry> inquiryList = inquiryDao.selectAll();
        List<AdminInquiryRes> resList = new ArrayList<>();

        for (Inquiry inq : inquiryList) {
            AdminInquiryRes res = new AdminInquiryRes();
            res.setInquiryId(inq.getInquiryId());
            res.setUserId(inq.getUserId());
            res.setUserName(inq.getUserName());
            res.setPhoneNum(inq.getPhoneNum());
            res.setInquiryType(inq.getInquiryType());
            res.setInquiryContents(inq.getInquiryContents());
            res.setAnswerContents(inq.getAnswerContents());
            res.setAnswerUserName(inq.getAnswerUserName());
            if (inq.getCreateTime() != null) {
                res.setCreateTime(DATETIME_FORMAT.format(inq.getCreateTime()));
            }
            if (inq.getAnswerTime() != null) {
                res.setAnswerTime(DATETIME_FORMAT.format(inq.getAnswerTime()));
            }
            res.setProcessedFlg(inq.getProcessedFlg() != null && inq.getProcessedFlg() == 1);
            res.setDeleteFlg(inq.getDeleteFlg() != null && inq.getDeleteFlg() == 1);
            resList.add(res);
        }

        ListRes<AdminInquiryRes> listRes = new ListRes<>(resList, resList.size());
        return ResponseUtils.generateDtoSuccess(
                new Information(MessageIdConst.I_GETTING_SUCCESS, "SUCCESS"),
                listRes
        );
    }

    /**
     * 문의 목록 페이징 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getInquiryListPaged(String operatorUserId, AdminInquirySearchReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (req == null) req = new AdminInquirySearchReq();
        long totalCount = inquiryDao.selectInquiryCount(req);
        List<AdminInquiryRes> inquiries = totalCount > 0
                ? inquiryDao.selectInquiryListPaged(req)
                : Collections.emptyList();
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new PageRes<>(inquiries, totalCount, req.getPage(), req.getSize()));
    }

    /**
     * 문의 목록 대용량 엑셀 스트리밍 다운로드
     */
    @Transactional(readOnly = true)
    public void exportInquiriesExcel(String operatorUserId, AdminInquirySearchReq req, HttpServletResponse response) throws IOException {
        if (isNotAdmin(operatorUserId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "ACCESS_DENIED");
            return;
        }
        if (req == null) req = new AdminInquirySearchReq();
        req.setSize(null);
        List<AdminInquiryRes> inquiries = inquiryDao.selectInquiryListPaged(req);
        if (inquiries != null) {
            for (AdminInquiryRes inq : inquiries) {
                inq.setStatusText(inq.getStatusText());
            }
        }
        ExcelStreamingUtil.export(response, "문의내역", inquiries, AdminInquiryRes.class);
    }

    /**
     * 답변 대기 문의 요약 (대시보드 알림 배너용: 대기 건수 및 최신 1건만 경량 조회)
     */
    @Transactional(readOnly = true)
    public BaseResponse getPendingInquirySummary(String operatorUserId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }

        int count = inquiryDao.countPendingInquiries();
        AdminInquirySummaryRes res = new AdminInquirySummaryRes();
        res.setPendingCount(count);

        if (count > 0) {
            Inquiry latest = inquiryDao.selectLatestPendingInquiry();
            if (latest != null) {
                AdminInquiryRes itemRes = new AdminInquiryRes();
                itemRes.setInquiryId(latest.getInquiryId());
                itemRes.setUserId(latest.getUserId());
                itemRes.setUserName(latest.getUserName());
                itemRes.setPhoneNum(latest.getPhoneNum());
                itemRes.setInquiryType(latest.getInquiryType());
                itemRes.setInquiryContents(latest.getInquiryContents());
                if (latest.getCreateTime() != null) {
                    itemRes.setCreateTime(DATETIME_FORMAT.format(latest.getCreateTime()));
                }
                itemRes.setProcessedFlg(false);
                itemRes.setDeleteFlg(false);
                res.setLatestPendingInquiry(itemRes);
            }
        }

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 문의 단건 상세 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getInquiryDetail(String operatorUserId, String inquiryId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (inquiryId == null || inquiryId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_INQUIRY_ID", "INVALID_INQUIRY_ID"));
        }

        Inquiry inq = inquiryDao.selectByPrimaryKey(inquiryId.trim());
        if (inq == null || (inq.getDeleteFlg() != null && inq.getDeleteFlg() == 1)) {
            return ResponseUtils.generateDtoFailed(new Information("NOT_FOUND", "NOT_FOUND"));
        }

        AdminInquiryRes res = new AdminInquiryRes();
        res.setInquiryId(inq.getInquiryId());
        res.setUserId(inq.getUserId());
        res.setUserName(inq.getUserName());
        res.setPhoneNum(inq.getPhoneNum());
        res.setInquiryType(inq.getInquiryType());
        res.setInquiryContents(inq.getInquiryContents());
        res.setAnswerContents(inq.getAnswerContents());
        res.setAnswerUserName(inq.getAnswerUserName());
        if (inq.getCreateTime() != null) {
            res.setCreateTime(DATETIME_FORMAT.format(inq.getCreateTime()));
        }
        if (inq.getAnswerTime() != null) {
            res.setAnswerTime(DATETIME_FORMAT.format(inq.getAnswerTime()));
        }
        res.setProcessedFlg(inq.getProcessedFlg() != null && inq.getProcessedFlg() == 1);
        res.setDeleteFlg(inq.getDeleteFlg() != null && inq.getDeleteFlg() == 1);

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 문의 답변 작성 및 상태 변경
     */
    @Transactional
    public BaseResponse answerInquiry(String operatorUserId, String inquiryId, AdminInquiryAnswerReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (inquiryId == null || inquiryId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_INQUIRY_ID", "INVALID_INQUIRY_ID"));
        }

        Inquiry inq = inquiryDao.selectByPrimaryKey(inquiryId.trim());
        if (inq == null) {
            return ResponseUtils.generateDtoFailed(new Information("NOT_FOUND", "NOT_FOUND"));
        }

        User operator = userDao.findUser(operatorUserId.trim());
        String answerUserName = (operator != null && operator.getUserName() != null) ? operator.getUserName() : "관리자";

        inq.setAnswerContents(req.getAnswerContents());
        inq.setAnswerUserName(answerUserName);
        inq.setAnswerTime(new Date());
        inq.setProcessedFlg((byte) (Boolean.TRUE.equals(req.getProcessedFlg()) ? 1 : 0));

        inquiryDao.updateByPrimaryKey(inq);

        ActionRes res = new ActionRes(inquiryId);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 문의 삭제 (소프트 삭제)
     */
    @Transactional
    public BaseResponse deleteInquiry(String operatorUserId, String inquiryId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (inquiryId == null || inquiryId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_INQUIRY_ID", "INVALID_INQUIRY_ID"));
        }

        Inquiry inq = inquiryDao.selectByPrimaryKey(inquiryId.trim());
        if (inq == null) {
            return ResponseUtils.generateDtoFailed(new Information("NOT_FOUND", "NOT_FOUND"));
        }

        inq.setDeleteFlg((byte) 1);
        inquiryDao.updateByPrimaryKey(inq);

        ActionRes res = new ActionRes(inquiryId);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    // ── [5. 시공 보고서 관리] ──────────────────────────────────────────

    /**
     * 시공 보고서 목록 조회 (관리자용, 다중 필터 지원)
     */
    @Transactional(readOnly = true)
    public BaseResponse getReportList(String operatorUserId, AdminReportSearchReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }

        List<WorkReportRes> list = workReportDao.selectReportList(req);

        if (list == null) {
            list = new ArrayList<>();
        } else {
            for (WorkReportRes res : list) {
                enrichReportRes(res);
            }
        }

        long totalCount = (req != null && req.getLimit() != null)
                ? workReportDao.selectReportCount(req)
                : list.size();

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(list, (int) totalCount));
    }

    /**
     * 시공 보고서 목록 페이징 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getReportListPaged(String operatorUserId, AdminReportSearchReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (req == null) req = new AdminReportSearchReq();
        if (req.getPage() <= 0) req.setPage(1);
        if (req.getSize() == null || req.getSize() <= 0) req.setSize(30);
        long totalCount = workReportDao.selectReportCount(req);
        List<WorkReportRes> list = totalCount > 0
                ? workReportDao.selectReportList(req)
                : Collections.emptyList();
        for (WorkReportRes res : list) {
            enrichReportRes(res);
        }
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new PageRes<>(list, totalCount, req.getPage(), req.getSize()));
    }

    /**
     * 시공 보고서 목록 대용량 엑셀 스트리밍 다운로드
     */
    @Transactional(readOnly = true)
    public void exportReportsExcel(String operatorUserId, AdminReportSearchReq req, HttpServletResponse response) throws IOException {
        if (isNotAdmin(operatorUserId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "ACCESS_DENIED");
            return;
        }
        if (req == null) req = new AdminReportSearchReq();
        req.setSize(null);
        req.setLimit(null);
        List<WorkReportRes> list = workReportDao.selectReportList(req);
        if (list != null) {
            for (WorkReportRes res : list) {
                enrichReportRes(res);
            }
        } else {
            list = Collections.emptyList();
        }
        ExcelStreamingUtil.export(response, "시공보고서목록", list, WorkReportRes.class);
    }

    /**
     * 작업자 실적 랭킹 목록 조회 (대시보드 전용: limit 지원)
     */
    @Transactional(readOnly = true)
    public BaseResponse getWorkerRanking(String operatorUserId, String regionId, Integer limit) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (limit == null || limit <= 0) limit = 10;
        List<AdminWorkerStatRes> list = workReportDao.selectWorkerRanking(regionId, limit);
        long totalCount = workReportDao.selectWorkerRankingCount(regionId);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(list != null ? list : Collections.emptyList(), (int) totalCount));
    }

    @Transactional(readOnly = true)
    public BaseResponse getDashboardSummary(String operatorUserId, String regionId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }

        Map<String, Object> householdMap = siteDao.selectRegionalHouseholdSummary(regionId);
        Map<String, Object> reportMap = workReportDao.selectReportSummary(regionId);
        long totalWorkers = workReportDao.selectWorkerRankingCount(regionId);

        AdminDashboardSummaryRes res = new AdminDashboardSummaryRes();
        if (householdMap != null) {
            res.setTotalSites(((Number) householdMap.getOrDefault("totalSites", 0L)).longValue());
            res.setTotalTarget(((Number) householdMap.getOrDefault("totalTarget", 0L)).longValue());
            res.setCompletedTarget(((Number) householdMap.getOrDefault("completedTarget", 0L)).longValue());
        }
        if (reportMap != null) {
            res.setTotalReports(((Number) reportMap.getOrDefault("totalReports", 0L)).longValue());
            res.setTodayReports(((Number) reportMap.getOrDefault("todayReports", 0L)).longValue());
            res.setPendingReports(((Number) reportMap.getOrDefault("pendingReports", 0L)).longValue());
            res.setRejectedReports(((Number) reportMap.getOrDefault("rejectedReports", 0L)).longValue());
            res.setCompletedReports(((Number) reportMap.getOrDefault("completedReports", 0L)).longValue());
            res.setIssueReportsCount(((Number) reportMap.getOrDefault("issueReportsCount", 0L)).longValue());
        }
        res.setTotalWorkers(totalWorkers);
        if (res.getTotalTarget() > 0) {
            res.setProgressRate((int) Math.round(((double) res.getCompletedTarget() / res.getTotalTarget()) * 100));
        }

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 시공 보고서 단건 상세 조회 (관리자용)
     */
    @Transactional(readOnly = true)
    public BaseResponse getReportDetail(String operatorUserId, String reportId) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (reportId == null || reportId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PARAMETER", "REPORT_ID_REQUIRED"));
        }

        WorkReportRes detail = workReportDao.selectReportDetailById(reportId.trim());
        if (detail == null) {
            return ResponseUtils.generateDtoFailed(new Information("NOT_FOUND", "REPORT_NOT_FOUND"));
        }
        enrichReportRes(detail);

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, detail);
    }

    /**
     * 시공 보고서 상태 변경 (승인 COMPLETED, 반려 REJECTED + fixReason, 검토대기 PENDING)
     */
    @Transactional
    public BaseResponse updateReportStatus(String operatorUserId, String reportId, AdminReportStatusReq req) {
        if (isNotAdmin(operatorUserId)) {
            return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "ACCESS_DENIED"));
        }
        if (reportId == null || reportId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PARAMETER", "REPORT_ID_REQUIRED"));
        }
        if (req == null || req.getStatus() == null || req.getStatus().trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PARAMETER", "STATUS_REQUIRED"));
        }

        String newStatus = req.getStatus().trim().toUpperCase();
        String fixReason = req.getFixReason() != null ? req.getFixReason().trim() : "";

        if ("REJECTED".equals(newStatus) && fixReason.isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("FIX_REASON_REQUIRED", "FIX_REASON_REQUIRED"));
        }

        WorkReportRes existing = workReportDao.selectReportDetailById(reportId.trim());
        if (existing == null) {
            return ResponseUtils.generateDtoFailed(new Information("NOT_FOUND", "REPORT_NOT_FOUND"));
        }

        workReportDao.updateReportStatus(reportId.trim(), newStatus, fixReason);

        // 보고서 상태 변경에 따른 해당 세대(Household)의 install_status 동기화
        if (existing.getHouseholdId() != null && !existing.getHouseholdId().trim().isEmpty()) {
            Household hh = siteDao.selectHouseholdById(existing.getHouseholdId().trim());
            if (hh != null) {
                if ("COMPLETED".equals(newStatus)) {
                    hh.setInstallStatus("INSTALLED");
                } else if ("REJECTED".equals(newStatus)) {
                    hh.setInstallStatus("HOLD");
                } else if ("PENDING".equals(newStatus)) {
                    hh.setInstallStatus("UNINSTALLED");
                } else {
                    hh.setInstallStatus("UNINSTALLED");
                }
                siteDao.updateHousehold(hh);
            }
        }

        ActionRes res = new ActionRes(reportId);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    private void enrichReportRes(WorkReportRes res) {
        if (res == null) return;
        if (res.getInstallDate() != null && !res.getInstallDate().trim().isEmpty()) {
            try {
                Date d = DATE_FORMAT.parse(res.getInstallDate().trim());
                SimpleDateFormat koreanDateFmt = new SimpleDateFormat("yyyy년 M월 d일");
                res.setInstallDateFormatted(koreanDateFmt.format(d));
            } catch (Exception ignored) {}
        }
        if (res.getSubmittedAt() == null && res.getReportTime() != null) {
            res.setSubmittedAt(res.getReportTime());
        }
    }

    private String formatPhoneNumber(String phone) {
        if (phone == null) return null;
        String digits = phone.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return "";

        // 전국 대표번호 (15xx, 16xx 등 8자리)
        if (digits.startsWith("1") && digits.length() == 8) {
            return digits.replaceFirst("(\\d{4})(\\d{4})", "$1-$2");
        }
        // 평생/안심번호 (050x 11~12자리)
        if (digits.startsWith("050")) {
            if (digits.length() == 12) {
                return digits.replaceFirst("(\\d{4})(\\d{4})(\\d{4})", "$1-$2-$3");
            } else if (digits.length() == 11) {
                return digits.replaceFirst("(\\d{4})(\\d{3})(\\d{4})", "$1-$2-$3");
            }
        }
        // 서울 지역 유선전화 (02)
        if (digits.startsWith("02")) {
            if (digits.length() == 10) {
                return digits.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "$1-$2-$3");
            } else if (digits.length() == 9) {
                return digits.replaceFirst("(\\d{2})(\\d{3})(\\d{4})", "$1-$2-$3");
            }
        }
        // 전국 지역 유선전화(031~064), 휴대폰(010 등), 인터넷전화(070)
        if (digits.length() == 11) {
            return digits.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        } else if (digits.length() == 10) {
            return digits.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        }

        return phone.trim();
    }
}