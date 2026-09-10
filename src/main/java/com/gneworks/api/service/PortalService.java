package com.gneworks.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.id.KsuidGenerator;
import com.gneworks.common.utils.ResponseUtils;
import com.gneworks.common.utils.S3Utils;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.InquiryDao;
import com.gneworks.dao.SiteDao;
import com.gneworks.dao.SystemSettingsDao;
import com.gneworks.dao.UserDao;
import com.gneworks.dao.WorkReportDao;
import com.gneworks.dao.entity.*;
import com.gneworks.dto.res.ActionRes;
import com.gneworks.dto.res.AdminDashboardSummaryRes;
import com.gneworks.dto.res.AdminSiteRes;
import com.gneworks.dto.res.HouseholdRes;
import com.gneworks.dto.res.ListRes;
import com.gneworks.dto.res.PageRes;
import com.gneworks.dto.res.PortalNoticeRes;
import com.gneworks.dto.res.UserAssignedRegionDetailRes;
import java.util.Collections;
import com.gneworks.dto.res.UserRes;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gneworks.dto.req.AdminReportSearchReq;
import com.gneworks.dto.req.WorkReportReq;
import com.gneworks.dto.res.WorkReportRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PortalService {

    private static final Information INFO_SUCCESS = new Information("SUCCESS", "SUCCESS");

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private WorkReportDao workReportDao;

    @Autowired
    private InquiryDao inquiryDao;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppNotificationService appNotificationService;

    @Autowired
    private SystemSettingsDao systemSettingsDao;

    @Value("${cloud.aws.s3.prefix.user}")
    private String userPrefix;

    @Value("${cloud.aws.s3.prefix.report}")
    private String reportPrefix;

    // ── [1. 프로필 관리 (본인 전용)] ──────────────────────────────

    /**
     * 본인 프로필 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getProfile(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER", "LOGIN_REQUIRED"));
        }

        User user = userDao.findUser(userId);
        if (user == null) {
            return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.E_USER_NOT_FOUND,
                    messageSource.getMessage(MessageIdConst.E_USER_NOT_FOUND, null, LocaleAspect.LOCALE)));
        }

        // 프로필 이미지 경로 호환성 처리
        if (user.getProfileImg() != null && !user.getProfileImg().startsWith("/") && !user.getProfileImg().startsWith("http")) {
            user.setProfileImg("/" + user.getProfileImg());
        }

        UserRes res = new UserRes();
        BeanUtils.copyProperties(user, res);

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_GETTING_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_GETTING_SUCCESS, new String[] { "User" }, LocaleAspect.LOCALE)), res);
    }

    /**
     * 본인 프로필 수정 (연락처 및 프로필 사진만 수정 가능)
     */
    @Transactional
    public BaseResponse updateProfile(String userId, Map<String, Object> updates) {
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER", "LOGIN_REQUIRED"));
        }

        User user = userDao.findUser(userId);
        if (user == null) {
            return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.E_USER_NOT_FOUND,
                    messageSource.getMessage(MessageIdConst.E_USER_NOT_FOUND, null, LocaleAspect.LOCALE)));
        }

        // 오직 연락처(phoneNum)와 프로필사진(profileImg)만 변경 허용
        if (updates.containsKey("phoneNum")) {
            user.setPhoneNum((String) updates.get("phoneNum"));
        }

        String oldProfileImgToDelete = null;

        if (updates.containsKey("profileImg")) {
            String profileImg = (String) updates.get("profileImg");
            if (profileImg != null && profileImg.startsWith("data:image")) {
                try {
                    String[] parts = profileImg.split(",");
                    String header = parts[0];
                    String base64Data = parts[1];
                    String contentType = header.substring(header.indexOf(":") + 1, header.indexOf(";"));
                    String extension = contentType.contains("/") ? contentType.split("/")[1] : "webp";
                    byte[] bytes = Base64.getDecoder().decode(base64Data);

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String separator = userPrefix.endsWith("/") ? "" : "/";
                    String fileName = userPrefix + separator + userId + "_" + timeStamp + "." + extension;
                    String s3Path = "/" + S3Utils.uploadFile(fileName, bytes, contentType, amazonS3);

                    // 새 사진 업로드 성공 시, 이전 S3 사진을 삭제 대상으로 예약
                    if (user.getProfileImg() != null && !user.getProfileImg().trim().isEmpty() && !user.getProfileImg().startsWith("data:image")) {
                        oldProfileImgToDelete = user.getProfileImg();
                    }
                    user.setProfileImg(s3Path);
                } catch (Exception e) {
                    log.error("Failed to upload profile image to S3 for user: {}", userId, e);
                    return ResponseUtils.generateDtoFailed(new Information("IMAGE_UPLOAD_FAILED", "Failed to upload profile image"));
                }
            } else {
                if (profileImg == null || profileImg.trim().isEmpty()) {
                    if (user.getProfileImg() != null && !user.getProfileImg().trim().isEmpty() && !user.getProfileImg().startsWith("data:image")) {
                        oldProfileImgToDelete = user.getProfileImg();
                    }
                }
                user.setProfileImg(profileImg);
            }
        }

        userDao.updateProfile(user);

        // DB 저장까지 성공한 후 이전 프로필 사진을 안전하게 S3에서 삭제
        if (oldProfileImgToDelete != null) {
            try {
                String oldS3Key = oldProfileImgToDelete.startsWith("/") ? oldProfileImgToDelete.substring(1) : oldProfileImgToDelete;
                S3Utils.deleteFile(oldS3Key, amazonS3);
            } catch (Exception ex) {
                log.warn("Failed to delete old profile image from S3: {}", oldProfileImgToDelete, ex);
            }
        }

        // 업데이트된 사용자 프로필 반환
        UserRes res = new UserRes();
        BeanUtils.copyProperties(user, res);

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_UPDATE_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_UPDATE_SUCCESS, new String[] { "User" }, LocaleAspect.LOCALE)), res);
    }

    /**
     * 본인 비밀번호 변경
     */
    @Transactional
    public BaseResponse changePassword(String userId, String currentPassword, String newPassword) {
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER", "LOGIN_REQUIRED"));
        }

        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("CURRENT_PASSWORD_REQUIRED", "현재 비밀번호를 입력해 주세요."));
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("NEW_PASSWORD_REQUIRED", "새 비밀번호를 입력해 주세요."));
        }

        if (newPassword.trim().length() < 6) {
            return ResponseUtils.generateDtoFailed(new Information("PASSWORD_TOO_SHORT", "새 비밀번호는 최소 6자 이상이어야 합니다."));
        }

        User user = userDao.findUser(userId);
        if (user == null) {
            return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.E_USER_NOT_FOUND,
                    messageSource.getMessage(MessageIdConst.E_USER_NOT_FOUND, null, LocaleAspect.LOCALE)));
        }

        if (!passwordEncoder.matches(currentPassword, user.getUserPw())) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_CURRENT_PASSWORD", "현재 비밀번호가 일치하지 않습니다."));
        }

        if (currentPassword.equals(newPassword.trim())) {
            return ResponseUtils.generateDtoFailed(new Information("SAME_AS_OLD_PASSWORD", "기존 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다."));
        }

        String encodedNewPw = passwordEncoder.encode(newPassword.trim());
        userDao.updatePassword(userId, encodedNewPw);

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ActionRes(userId));
    }

    // ── [2. 담당 지역 관리 (본인 전용)] ────────────────────────────

    /**
     * 본인의 배정 관할 목록 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getAssignedRegions(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER", "LOGIN_REQUIRED"));
        }

        List<UserAssignedRegionDetailRes> list = siteDao.selectAssignedRegionsByUserId(userId);
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.removeIf(r -> r == null || r.getAssignedRegionId() == null);
        }
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(list));
    }

    /**
     * 본인에게 소방관할 배정 등록
     */
    @Transactional
    public BaseResponse assignRegion(String userId, String sidoName, String regionName) {
        return assignRegion(userId, null, sidoName, regionName);
    }

    @Transactional
    public BaseResponse assignRegion(String userId, String regionId, String sidoName, String regionName) {
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER", "LOGIN_REQUIRED"));
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

        // 중복 배정 여부 검증 (이미 배정된 경우 중복 insert 방지)
        List<UserAssignedRegionDetailRes> existing = siteDao.selectAssignedRegionsByUserId(userId);
        if (existing != null) {
            for (UserAssignedRegionDetailRes item : existing) {
                if (item != null && fireRegion.getRegionId().equals(item.getRegionId())) {
                    // 이미 배정되어 있으므로 기존 assignedRegionId 반환하며 성공 처리
                    ActionRes res = new ActionRes(item.getAssignedRegionId());
                    return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
                }
            }
        }

        UserAssignedRegion uar = new UserAssignedRegion();
        uar.setAssignedRegionId(KsuidGenerator.createId());
        uar.setUserId(userId);
        uar.setRegionId(fireRegion.getRegionId());
        uar.setCreateTime(new Date());

        siteDao.insertAssignedRegion(uar);

        ActionRes res = new ActionRes(uar.getAssignedRegionId());
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 본인의 소방관할 배정 해제
     * id: 소방관할 고유키(regionId) 또는 배정키(assignedRegionId) 모두 지원
     */
    @Transactional
    public BaseResponse unassignRegion(String userId, String id) {
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER", "LOGIN_REQUIRED"));
        }
        if (id == null || id.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_ID", "REGION_ID_REQUIRED"));
        }

        // 1. region_id로 삭제 시도
        int deleted = siteDao.deleteAssignedRegionByUserIdAndRegionId(userId, id.trim());
        if (deleted == 0) {
            // 2. assignedRegionId로 삭제 시도
            siteDao.deleteAssignedRegion(id.trim());
        }

        ActionRes res = new ActionRes(id);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 전체 소방관할 목록 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getFireRegions() {
        List<FireRegion> list = siteDao.selectAllFireRegions();
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(list));
    }

    /**
     * 현장 목록 조회 (세대 목록 포함 여부 선택 가능)
     */
    @Transactional(readOnly = true)
    public BaseResponse getSites(String regionId, String query, Integer limit, Boolean includeHouseholds) {
        List<AdminSiteRes> list = siteDao.selectSiteList(regionId, query, limit, null);
        long totalCount = limit != null
                ? siteDao.selectSiteListCount(regionId, query)
                : (list != null ? list.size() : 0);
        if (list == null) {
            list = new ArrayList<>();
        }

        if (Boolean.TRUE.equals(includeHouseholds)) {
            for (AdminSiteRes site : list) {
                if (site != null && site.getSiteId() != null) {
                    List<HouseholdRes> households = siteDao.selectHouseholdsBySiteId(site.getSiteId());
                    site.setHouseholds(households != null ? households : new ArrayList<>());
                }
            }
        }

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(list, (int) totalCount));
    }

    /**
     * 권역별 세대수 및 현장수 요약 집계
     */
    @Transactional(readOnly = true)
    public BaseResponse getRegionSummary(String regionId) {
        Map<String, Object> summary = siteDao.selectRegionalHouseholdSummary(regionId);
        AdminDashboardSummaryRes res = new AdminDashboardSummaryRes();
        if (summary != null) {
            res.setTotalSites(((Number) summary.getOrDefault("totalSites", 0L)).longValue());
            res.setTotalTarget(((Number) summary.getOrDefault("totalTarget", 0L)).longValue());
            res.setCompletedTarget(((Number) summary.getOrDefault("completedTarget", 0L)).longValue());
            if (res.getTotalTarget() > 0) {
                res.setProgressRate((int) Math.round((double) res.getCompletedTarget() / res.getTotalTarget() * 100));
            }
        }
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }

    /**
     * 현장 상세 조회 (세대 목록 포함)
     */
    @Transactional(readOnly = true)
    public BaseResponse getSiteDetail(String siteId) {
        if (siteId == null || siteId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PARAMETER", "SITE_ID_REQUIRED"));
        }
        AdminSiteRes detail = siteDao.selectSiteDetailWithHouseholds(siteId.trim());
        if (detail == null) {
            return ResponseUtils.generateDtoFailed(new Information("SITE_NOT_FOUND", "SITE_NOT_FOUND"));
        }
        if (detail.getHouseholds() == null) {
            detail.setHouseholds(new ArrayList<>());
        }
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, detail);
    }

    // ── [4. 시공 보고서 (WorkReport) 관리] ──────────────────────────

    /**
     * Base64 이미지 데이터를 S3에 업로드하고 발급된 S3 URL을 반환 (이미 URL이면 그대로 반환)
     */
    private String uploadBase64Image(String dataUrl, String prefix, String fileTag) {
        if (dataUrl == null || dataUrl.trim().isEmpty() || !dataUrl.startsWith("data:image")) {
            return dataUrl;
        }
        try {
            String[] parts = dataUrl.split(",");
            String header = parts[0];
            String base64Data = parts[1];
            String contentType = header.substring(header.indexOf(":") + 1, header.indexOf(";"));
            String extension = contentType.contains("/") ? contentType.split("/")[1] : "webp";
            byte[] bytes = Base64.getDecoder().decode(base64Data);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String separator = prefix.endsWith("/") ? "" : "/";
            String fullPath = prefix + separator + fileTag + "_" + timeStamp + "." + extension;
            return "/" + S3Utils.uploadFile(fullPath, bytes, contentType, amazonS3);
        } catch (Exception e) {
            log.error("Failed to upload report image to S3 (fileTag: {})", fileTag, e);
            throw new RuntimeException("Image upload failed: " + fileTag, e);
        }
    }

    private void checkAndQueueOldPhoto(String oldUrl, String newUrl, List<String> deleteQueue) {
        if (oldUrl != null && !oldUrl.trim().isEmpty() && !oldUrl.startsWith("data:image")) {
            if (newUrl != null && !oldUrl.equals(newUrl)) {
                deleteQueue.add(oldUrl);
            }
        }
    }

    /**
     * 시공 보고서 등록 및 수정 (UPSERT)
     */
    @Transactional
    public BaseResponse submitReport(String userId, WorkReportReq req) {
        if (req.getHouseholdId() == null || req.getHouseholdId().trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PARAMETER", "HOUSEHOLD_ID_REQUIRED"));
        }
        if (req.getSiteId() == null || req.getSiteId().trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PARAMETER", "SITE_ID_REQUIRED"));
        }

        String householdId = req.getHouseholdId().trim();
        String reportSubDir = reportPrefix + householdId + "/";

        WorkReport existing = workReportDao.selectByHouseholdId(householdId);

        // 다른 작업자가 이미 작성한 보고서는 수정 불가
        if (existing != null && existing.getUserId() != null && !existing.getUserId().trim().isEmpty()) {
            if (!existing.getUserId().trim().equals(userId.trim())) {
                return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "다른 작업자가 이미 제출한 세대 보고서는 수정할 수 없습니다."));
            }
        }

        // 서명 및 5종 사진 S3 업로드 (Base64 -> S3 URL 치환)
        String confirmerSignature = uploadBase64Image(req.getConfirmerSignature(), reportSubDir, "sig");
        String photoDoor = uploadBase64Image(req.getPhotoDoor(), reportSubDir, "door");
        String photoBefore1 = uploadBase64Image(req.getPhotoBefore1(), reportSubDir, "before1");
        String photoAfter1 = uploadBase64Image(req.getPhotoAfter1(), reportSubDir, "after1");
        String photoBefore2 = uploadBase64Image(req.getPhotoBefore2(), reportSubDir, "before2");
        String photoAfter2 = uploadBase64Image(req.getPhotoAfter2(), reportSubDir, "after2");

        // 기존 보고서가 있을 경우, 새로 교체되어 사용되지 않게 된 이전 S3 사진들을 수집
        List<String> oldPhotosToDelete = new ArrayList<>();
        if (existing != null) {
            checkAndQueueOldPhoto(existing.getConfirmerSignature(), confirmerSignature, oldPhotosToDelete);
            checkAndQueueOldPhoto(existing.getPhotoDoor(), photoDoor, oldPhotosToDelete);
            checkAndQueueOldPhoto(existing.getPhotoBefore1(), photoBefore1, oldPhotosToDelete);
            checkAndQueueOldPhoto(existing.getPhotoAfter1(), photoAfter1, oldPhotosToDelete);
            checkAndQueueOldPhoto(existing.getPhotoBefore2(), photoBefore2, oldPhotosToDelete);
            checkAndQueueOldPhoto(existing.getPhotoAfter2(), photoAfter2, oldPhotosToDelete);
        }

        Date installDate = null;
        if (req.getInstallDate() != null && !req.getInstallDate().trim().isEmpty()) {
            try {
                installDate = new SimpleDateFormat("yyyy-MM-dd").parse(req.getInstallDate().trim());
            } catch (Exception e) {
                log.warn("Invalid installDate format: {}", req.getInstallDate());
            }
        }
        if (installDate == null) {
            installDate = new Date();
        }

        Date now = new Date();

        if (existing == null) {
            // 신규 등록
            WorkReport report = new WorkReport();
            report.setReportId(KsuidGenerator.createId());
            report.setHouseholdId(householdId);
            report.setSiteId(req.getSiteId().trim());
            report.setUserId(userId);
            report.setDong(req.getDong() != null ? req.getDong() : "");
            report.setHo(req.getHo() != null ? req.getHo() : "");
            report.setHeadName(req.getHeadName() != null ? req.getHeadName() : "");
            report.setInstallDate(installDate);
            report.setReportTime(now);
            report.setReporterName(req.getReporterName() != null ? req.getReporterName() : "");
            report.setConfirmerName(req.getConfirmerName() != null ? req.getConfirmerName() : "");
            report.setConfirmerSignature(confirmerSignature != null ? confirmerSignature : "");
            report.setPhotoDoor(photoDoor);
            report.setPhotoBefore1(photoBefore1);
            report.setPhotoAfter1(photoAfter1);
            report.setPhotoBefore2(photoBefore2);
            report.setPhotoAfter2(photoAfter2);
            report.setStatus(req.getStatus() != null && !req.getStatus().trim().isEmpty() ? req.getStatus().trim() : "PENDING");
            report.setRemarks(req.getRemarks());
            report.setCreateTime(now);
            report.setLastUpdate(now);
            report.setDeleteFlg(false);

            workReportDao.insert(report);

            WorkReportRes res = workReportDao.selectReportDetailById(report.getReportId());
            enrichReportRes(res);

            // 신규 보고서 제출 시 관리자 전원에게 실시간 SSE 및 웹 푸시 알림 발송
            try {
                String siteName = (res != null && res.getSiteName() != null) ? res.getSiteName().trim() : "현장";
                String sido = (res != null && res.getSido() != null) ? res.getSido().trim() : "";
                String sigungu = (res != null && res.getSigungu() != null) ? res.getSigungu().trim() : "";

                String regionPart = "";
                if (!sido.isEmpty() && !sigungu.isEmpty()) {
                    regionPart = sido + " " + sigungu;
                } else if (!sido.isEmpty()) {
                    regionPart = sido;
                } else if (!sigungu.isEmpty()) {
                    regionPart = sigungu;
                }

                String locationPrefix = regionPart.isEmpty() ? siteName : regionPart + " · " + siteName;
                String title = "신규 작업 보고서 제출";
                String message = String.format("[%s] %s동 %s호 보고서가 제출되었습니다.", locationPrefix, report.getDong(), report.getHo());
                String targetUrl = "/manage/work?reportId=" + report.getReportId();

                appNotificationService.sendNotificationToAdmins(title, message, targetUrl, "LOGO");
            } catch (Exception e) {
                log.error("Failed to notify admins of new report: {}", e.getMessage());
            }

            return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
        } else {
            // 기존 보고서 수정
            existing.setSiteId(req.getSiteId().trim());
            existing.setUserId(userId);
            if (req.getDong() != null) existing.setDong(req.getDong());
            if (req.getHo() != null) existing.setHo(req.getHo());
            if (req.getHeadName() != null) existing.setHeadName(req.getHeadName());
            existing.setInstallDate(installDate);
            existing.setReportTime(now);
            if (req.getReporterName() != null) existing.setReporterName(req.getReporterName());
            if (req.getConfirmerName() != null) existing.setConfirmerName(req.getConfirmerName());
            if (confirmerSignature != null) existing.setConfirmerSignature(confirmerSignature);
            if (photoDoor != null) existing.setPhotoDoor(photoDoor);
            if (photoBefore1 != null) existing.setPhotoBefore1(photoBefore1);
            if (photoAfter1 != null) existing.setPhotoAfter1(photoAfter1);
            if (photoBefore2 != null) existing.setPhotoBefore2(photoBefore2);
            if (photoAfter2 != null) existing.setPhotoAfter2(photoAfter2);
            existing.setStatus("PENDING"); // 수정 제출 시 재검토 대기 상태
            existing.setRemarks(req.getRemarks());
            existing.setLastUpdate(now);

            workReportDao.updateByPrimaryKey(existing);

            // 수정 제출 시 재검토 대기 상태이므로 기존 승인되었던 세대 설치 상태를 UNINSTALLED로 리셋
            Household hh = siteDao.selectHouseholdById(householdId);
            if (hh != null && "INSTALLED".equals(hh.getInstallStatus())) {
                hh.setInstallStatus("UNINSTALLED");
                siteDao.updateHousehold(hh);
            }

            // DB 업데이트 완료 후 교체된 이전 S3 사진 파일들 안전하게 삭제
            for (String oldPhotoPath : oldPhotosToDelete) {
                try {
                    String oldS3Key = oldPhotoPath.startsWith("/") ? oldPhotoPath.substring(1) : oldPhotoPath;
                    S3Utils.deleteFile(oldS3Key, amazonS3);
                } catch (Exception ex) {
                    log.warn("Failed to delete replaced report image from S3: {}", oldPhotoPath, ex);
                }
            }

            WorkReportRes res = workReportDao.selectReportDetailById(existing.getReportId());
            enrichReportRes(res);
            return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
        }
    }

    /**
     * 세대별 보고서 단건 조회 (본인 작성 보고서만 상세 조회 가능)
     */
    @Transactional(readOnly = true)
    public BaseResponse getReportByHouseholdId(String userId, String householdId) {
        if (householdId == null || householdId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PARAMETER", "HOUSEHOLD_ID_REQUIRED"));
        }
        WorkReportRes detail = workReportDao.selectReportDetailByHouseholdId(householdId.trim());
        if (detail == null) {
            return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, null);
        }
        // 다른 작업자가 작성한 보고서인 경우 열람 차단
        if (detail.getUserId() != null && !detail.getUserId().trim().isEmpty()) {
            if (userId != null && !userId.trim().isEmpty() && !detail.getUserId().trim().equals(userId.trim())) {
                return ResponseUtils.generateDtoFailed(new Information("ACCESS_DENIED", "다른 작업자가 이미 완료한 보고서입니다."));
            }
        }
        enrichReportRes(detail);
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, detail);
    }

    /**
     * 시공 보고서 목록 조회 (담당 지역 현장들 또는 본인 작성)
     */
    @Transactional(readOnly = true)
    public BaseResponse getReports(String userId, AdminReportSearchReq req) {
        if (req == null) {
            req = new AdminReportSearchReq();
        }

        // 특정 현장이나 소방관할이 지정되지 않은 경우, 작업자 본인 보고서 + 배정된 관할 지역 보고서 대상
        if ((req.getSiteId() == null || req.getSiteId().trim().isEmpty())
                && (req.getRegionId() == null || req.getRegionId().trim().isEmpty())) {
            req.setPortalUserId(userId);
            List<UserAssignedRegionDetailRes> assigned = siteDao.selectAssignedRegionsByUserId(userId);
            if (assigned != null && !assigned.isEmpty()) {
                List<String> regionIds = assigned.stream()
                        .map(UserAssignedRegionDetailRes::getRegionId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                req.setAssignedRegionIds(regionIds);
            }
        }

        boolean isPaged = (req.getPage() != null && req.getPage() > 0) || (req.getSize() != null && req.getSize() > 0);
        if (isPaged) {
            if (req.getPage() <= 0) req.setPage(1);
            if (req.getSize() == null || req.getSize() <= 0) req.setSize(20);
        }

        long totalCount = workReportDao.selectReportCount(req);
        List<WorkReportRes> list = totalCount > 0
                ? workReportDao.selectReportList(req)
                : Collections.emptyList();

        for (WorkReportRes res : list) {
            enrichReportRes(res);
        }

        if (isPaged) {
            return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new PageRes<>(list, totalCount, req.getPage(), req.getSize()));
        } else {
            return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(list, (int) totalCount));
        }
    }

    private void enrichReportRes(WorkReportRes res) {
        if (res == null) return;
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat koreanDateFmt = new SimpleDateFormat("yyyy년 M월 d일");
        SimpleDateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (res.getInstallDate() != null && !res.getInstallDate().trim().isEmpty() && res.getInstallDateFormatted() == null) {
            try {
                Date d = dateFmt.parse(res.getInstallDate().trim());
                res.setInstallDateFormatted(koreanDateFmt.format(d));
            } catch (Exception ignored) {}
        }
        if (res.getSubmittedAt() == null && res.getReportTime() != null) {
            res.setSubmittedAt(res.getReportTime());
        }
    }

    // ── [4. 문의 내역 관리 (본인 전용)] ────────────────────────────

    /**
     * 본인의 문의 및 답변 내역 목록 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getMyInquiries(String userId) {
        if (StringUtils.isBlank(userId)) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER", "LOGIN_REQUIRED"));
        }
        List<Inquiry> list = inquiryDao.selectByUserId(userId);
        return ResponseUtils.generateDtoSuccess(
                new Information(MessageIdConst.I_GETTING_SUCCESS,
                        messageSource.getMessage(MessageIdConst.I_GETTING_SUCCESS, new String[] { "Inquiry" }, LocaleAspect.LOCALE)),
                new ListRes<>(list != null ? list : new ArrayList<>()));
    }

    // ── [5. 현장 안내사항(공지)] ────────────────────────────

    /**
     * 현장 안내사항(공지) 및 비상 연락처 조회
     * GET /portal/notice
     */
    @Transactional(readOnly = true)
    public BaseResponse getNotice() {
        SystemSettings setting = systemSettingsDao.selectByPrimaryKey((byte) 1);
        if (setting == null) {
            return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, null);
        }

        PortalNoticeRes res = PortalNoticeRes.builder()
                .noticeVisible(setting.getNoticeVisible() != null ? setting.getNoticeVisible() : Boolean.TRUE)
                .noticeTitle(setting.getNoticeTitle())
                .noticeContent(setting.getNoticeContent())
                .noticeDate(setting.getNoticeDate())
                .contactPhone(setting.getContactPhone())
                .contactEmail(setting.getContactEmail())
                .build();

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, res);
    }
}