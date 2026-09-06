package com.gneworks.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.id.KsuidGenerator;
import com.gneworks.common.utils.ResponseUtils;
import com.gneworks.common.utils.S3Utils;
import com.gneworks.dao.SiteDao;
import com.gneworks.dao.UserDao;
import com.gneworks.dao.entity.FireRegion;
import com.gneworks.dao.entity.Household;
import com.gneworks.dao.entity.User;
import com.gneworks.dao.entity.UserAssignedRegion;
import com.gneworks.dto.res.ActionRes;
import com.gneworks.dto.res.AdminSiteRes;
import com.gneworks.dto.res.ListRes;
import com.gneworks.dto.res.UserAssignedRegionDetailRes;
import com.gneworks.dto.res.UserRes;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gneworks.dao.entity.WorkReport;
import com.gneworks.dto.req.WorkReportReq;
import com.gneworks.dto.res.WorkReportRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private com.gneworks.dao.WorkReportDao workReportDao;

    @Autowired
    private AmazonS3 amazonS3;

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

        if (updates.containsKey("profileImg")) {
            String profileImg = (String) updates.get("profileImg");
            if (profileImg != null && profileImg.startsWith("data:image")) {
                try {
                    String[] parts = profileImg.split(",");
                    String header = parts[0];
                    String base64Data = parts[1];
                    String contentType = header.substring(header.indexOf(":") + 1, header.indexOf(";"));
                    String extension = contentType.split("/")[1];
                    byte[] bytes = Base64.getDecoder().decode(base64Data);

                    String separator = userPrefix.endsWith("/") ? "" : "/";
                    String fileName = userPrefix + separator + userId + "_" + System.currentTimeMillis() + "." + extension;
                    String s3Path = "/" + S3Utils.uploadFile(fileName, bytes, contentType, amazonS3);
                    user.setProfileImg(s3Path);
                } catch (Exception e) {
                    log.error("Failed to upload profile image to S3 for user: {}", userId, e);
                    return ResponseUtils.generateDtoFailed(new Information("IMAGE_UPLOAD_FAILED", "Failed to upload profile image"));
                }
            } else {
                user.setProfileImg(profileImg);
            }
        }

        userDao.updateProfile(user);

        // 업데이트된 사용자 프로필 반환
        UserRes res = new UserRes();
        BeanUtils.copyProperties(user, res);

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_UPDATE_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_UPDATE_SUCCESS, new String[] { "User" }, LocaleAspect.LOCALE)), res);
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
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_USER", "LOGIN_REQUIRED"));
        }
        if (sidoName == null || sidoName.trim().isEmpty() || regionName == null || regionName.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_REGION", "SIDO_AND_REGION_REQUIRED"));
        }

        FireRegion fireRegion = siteDao.selectFireRegionBySidoAndName(sidoName, regionName);
        // "안산시" <-> "안산" 등 유연 매칭 지원
        if (fireRegion == null && regionName.length() > 1) {
            String trimmedName = regionName.replaceAll("(시|군|구)$", "");
            fireRegion = siteDao.selectFireRegionBySidoAndName(sidoName, trimmedName);
        }
        if (fireRegion == null) {
            fireRegion = siteDao.selectFireRegionBySidoAndName(sidoName, regionName + "시");
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
    public BaseResponse getSites(String sido, String sigungu, String eupmyeondong, String query, Boolean includeHouseholds) {
        List<AdminSiteRes> list = siteDao.selectSiteList(sido, sigungu, eupmyeondong, query);
        if (list == null) {
            list = new ArrayList<>();
        }

        if (Boolean.TRUE.equals(includeHouseholds)) {
            for (AdminSiteRes site : list) {
                if (site != null && site.getSiteId() != null) {
                    List<Household> households = siteDao.selectHouseholdsBySiteId(site.getSiteId());
                    site.setHouseholds(households != null ? households : new ArrayList<>());
                }
            }
        }

        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(list));
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

            String separator = prefix.endsWith("/") ? "" : "/";
            String fullPath = prefix + separator + fileTag + "_" + System.currentTimeMillis() + "." + extension;
            return "/" + S3Utils.uploadFile(fullPath, bytes, contentType, amazonS3);
        } catch (Exception e) {
            log.error("Failed to upload report image to S3 (fileTag: {})", fileTag, e);
            throw new RuntimeException("Image upload failed: " + fileTag, e);
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

        // 서명 및 5종 사진 S3 업로드 (Base64 -> S3 URL 치환)
        String confirmerSignature = uploadBase64Image(req.getConfirmerSignature(), reportSubDir, "sig");
        String photoDoor = uploadBase64Image(req.getPhotoDoor(), reportSubDir, "door");
        String photoBefore1 = uploadBase64Image(req.getPhotoBefore1(), reportSubDir, "before1");
        String photoAfter1 = uploadBase64Image(req.getPhotoAfter1(), reportSubDir, "after1");
        String photoBefore2 = uploadBase64Image(req.getPhotoBefore2(), reportSubDir, "before2");
        String photoAfter2 = uploadBase64Image(req.getPhotoAfter2(), reportSubDir, "after2");

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

        WorkReport existing = workReportDao.selectByHouseholdId(householdId);
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
            return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, convertToRes(report));
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
            return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, convertToRes(existing));
        }
    }

    /**
     * 세대별 보고서 단건 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getReportByHouseholdId(String householdId) {
        if (householdId == null || householdId.trim().isEmpty()) {
            return ResponseUtils.generateDtoFailed(new Information("INVALID_PARAMETER", "HOUSEHOLD_ID_REQUIRED"));
        }
        WorkReport report = workReportDao.selectByHouseholdId(householdId.trim());
        if (report == null) {
            return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, null);
        }
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, convertToRes(report));
    }

    /**
     * 내가 작성한 보고서 목록 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse getMyReports(String userId) {
        List<WorkReport> list = workReportDao.selectByUserId(userId);
        List<WorkReportRes> resList = new ArrayList<>();
        if (list != null) {
            for (WorkReport r : list) {
                resList.add(convertToRes(r));
            }
        }
        return ResponseUtils.generateDtoSuccess(INFO_SUCCESS, new ListRes<>(resList));
    }

    private WorkReportRes convertToRes(WorkReport report) {
        if (report == null) return null;
        WorkReportRes res = new WorkReportRes();
        BeanUtils.copyProperties(report, res);

        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (report.getInstallDate() != null) {
            res.setInstallDate(dateFmt.format(report.getInstallDate()));
        }
        if (report.getReportTime() != null) {
            res.setReportTime(timeFmt.format(report.getReportTime()));
        }
        return res;
    }
}
