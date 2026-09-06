package com.gneworks.api.controller;

import com.gneworks.api.controller.base.BaseController;
import com.gneworks.api.service.PortalService;
import com.gneworks.dto.res.core.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gneworks.dto.req.AdminReportSearchReq;
import com.gneworks.dto.req.WorkReportReq;
import java.util.Map;

@RestController
@RequestMapping("/portal")
@Slf4j
public class PortalController extends BaseController {

    @Autowired
    private PortalService portalService;

    // ── [1. 프로필 관리 (본인 전용)] ──────────────────────────────

    /**
     * 본인 프로필 조회
     * GET /portal/profile
     */
    @GetMapping("/profile")
    public BaseResponse getProfile() {
        return portalService.getProfile(getCurrentUserId());
    }

    /**
     * 본인 프로필 수정 (연락처 및 프로필 사진만 수정 가능)
     * PATCH /portal/profile
     */
    @PatchMapping("/profile")
    public BaseResponse updateProfile(@RequestBody Map<String, Object> updates) {
        return portalService.updateProfile(getCurrentUserId(), updates);
    }

    // ── [2. 담당 지역 관리 (본인 전용)] ────────────────────────────

    /**
     * 본인의 배정 관할 목록 조회
     * GET /portal/regions
     */
    @GetMapping("/regions")
    public BaseResponse getAssignedRegions() {
        return portalService.getAssignedRegions(getCurrentUserId());
    }

    /**
     * 본인에게 소방관할 배정 등록
     * POST /portal/regions
     */
    @PostMapping("/regions")
    public BaseResponse assignRegion(@RequestBody Map<String, String> body) {
        String sidoName = body != null ? body.get("sidoName") : null;
        String regionName = body != null ? body.get("regionName") : null;
        return portalService.assignRegion(getCurrentUserId(), sidoName, regionName);
    }

    /**
     * 본인의 소방관할 배정 해제
     * DELETE /portal/regions/{regionId}
     */
    @DeleteMapping("/regions/{regionId}")
    public BaseResponse unassignRegion(@PathVariable("regionId") String regionId) {
        return portalService.unassignRegion(getCurrentUserId(), regionId);
    }

    /**
     * 전체 소방관할 목록 조회
     * GET /portal/fire-regions
     */
    @GetMapping("/fire-regions")
    public BaseResponse getFireRegions() {
        return portalService.getFireRegions();
    }

    /**
     * 현장 목록 조회 (includeHouseholds=true 시 세대 목록 포함)
     * GET /portal/sites
     */
    @GetMapping("/sites")
    public BaseResponse getSites(
            @RequestParam(value = "sido", required = false) String sido,
            @RequestParam(value = "sigungu", required = false) String sigungu,
            @RequestParam(value = "eupmyeondong", required = false) String eupmyeondong,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "includeHouseholds", required = false, defaultValue = "true") Boolean includeHouseholds) {
        return portalService.getSites(sido, sigungu, eupmyeondong, query, includeHouseholds);
    }

    /**
     * 현장 상세 조회 (세대 목록 포함)
     * GET /portal/sites/{siteId}
     */
    @GetMapping("/sites/{siteId}")
    public BaseResponse getSiteDetail(@PathVariable("siteId") String siteId) {
        return portalService.getSiteDetail(siteId);
    }

    // ── [3. 시공 보고서 관리] ────────────────────────────

    /**
     * 시공 보고서 등록 및 수정 (UPSERT)
     * POST /portal/report
     */
    @PostMapping("/report")
    public BaseResponse submitReport(@RequestBody WorkReportReq req) {
        return portalService.submitReport(getCurrentUserId(), req);
    }

    /**
     * 세대별 시공 보고서 조회
     * GET /portal/report/{householdId}
     */
    @GetMapping("/report/{householdId}")
    public BaseResponse getReportByHouseholdId(@PathVariable("householdId") String householdId) {
        return portalService.getReportByHouseholdId(getCurrentUserId(), householdId);
    }

    /**
     * 시공 보고서 목록 조회 (담당 현장 또는 본인 작성)
     * GET /portal/reports
     */
    @GetMapping("/reports")
    public BaseResponse getReports(AdminReportSearchReq req) {
        return portalService.getReports(getCurrentUserId(), req);
    }

    // ── [4. 문의 내역 관리 (본인 전용)] ────────────────────────────

    /**
     * 본인의 문의 및 답변 내역 목록 조회
     * GET /portal/inquiries
     */
    @GetMapping("/inquiries")
    public BaseResponse getMyInquiries() {
        return portalService.getMyInquiries(getCurrentUserId());
    }
}