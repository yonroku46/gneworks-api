package com.gneworks.api.controller;

import com.gneworks.api.controller.base.BaseController;
import com.gneworks.api.service.AdminService;
import com.gneworks.aspect.attribute.CheckToken;
import com.gneworks.dto.req.AdminHouseholdReq;
import com.gneworks.dto.req.AdminInquiryAnswerReq;
import com.gneworks.dto.req.AdminSiteReq;
import com.gneworks.dto.req.AdminUserReq;
import com.gneworks.dto.res.core.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@CheckToken(required = true)
@Slf4j
public class AdminController extends BaseController {

    @Autowired
    private AdminService adminService;

    // ── [1. 계정 / 작업자 관리] ────────────────────────────────────

    /**
     * 작업자 목록 조회(관리자 제외)
     * GET /admin/user/list
     */
    @GetMapping("/user/list")
    public BaseResponse getUserList() {
        return adminService.getUserList(getCurrentUserId());
    }

    /**
     * 신규 작업자 계정 생성 (생년월일 초기 비밀번호)
     * POST /admin/user/create
     */
    @PostMapping("/user/create")
    public BaseResponse createUser(@RequestBody AdminUserReq req) {
        return adminService.createUser(getCurrentUserId(), req);
    }

    /**
     * 계정 기본정보 수정
     * PUT /admin/user/update
     */
    @PutMapping("/user/update")
    public BaseResponse updateUser(@RequestBody AdminUserReq req) {
        return adminService.updateUser(getCurrentUserId(), req);
    }

    /**
     * 비밀번호 초기화
     * POST /admin/user/reset-password
     */
    @PostMapping("/user/reset-password")
    public BaseResponse resetPassword(@RequestBody Map<String, String> body) {
        String targetUserId = body.get("userId");
        return adminService.resetPassword(getCurrentUserId(), targetUserId);
    }

    /**
     * 계정 비활성화(삭제)
     * DELETE /admin/user/{userId}
     */
    @DeleteMapping("/user/{userId}")
    public BaseResponse deleteUser(@PathVariable("userId") String targetUserId) {
        return adminService.deleteUser(getCurrentUserId(), targetUserId);
    }

    // ── [2. 현장 / 세대 관리] ────────────────────────────────────

    /**
     * 현장 목록 조회 (필터: sido, sigungu, eupmyeondong, query)
     * GET /admin/site/list
     */
    @GetMapping("/site/list")
    public BaseResponse getSiteList(
            @RequestParam(value = "sido", required = false) String sido,
            @RequestParam(value = "sigungu", required = false) String sigungu,
            @RequestParam(value = "eupmyeondong", required = false) String eupmyeondong,
            @RequestParam(value = "query", required = false) String query) {
        return adminService.getSiteList(getCurrentUserId(), sido, sigungu, eupmyeondong, query);
    }

    /**
     * 현장 상세 조회 (세대 목록 및 담당 작업자 포함)
     * GET /admin/site/{siteId}
     */
    @GetMapping("/site/{siteId}")
    public BaseResponse getSiteDetail(@PathVariable("siteId") String siteId) {
        return adminService.getSiteDetail(getCurrentUserId(), siteId);
    }

    /**
     * 신규 현장 등록
     * POST /admin/site
     */
    @PostMapping("/site")
    public BaseResponse createSite(@RequestBody AdminSiteReq req) {
        return adminService.createSite(getCurrentUserId(), req);
    }

    /**
     * 현장 정보 수정
     * PUT /admin/site/{siteId}
     */
    @PutMapping("/site/{siteId}")
    public BaseResponse updateSite(@PathVariable("siteId") String siteId, @RequestBody AdminSiteReq req) {
        return adminService.updateSite(getCurrentUserId(), siteId, req);
    }

    /**
     * 현장 삭제 (연관 세대 일괄 삭제)
     * DELETE /admin/site/{siteId}
     */
    @DeleteMapping("/site/{siteId}")
    public BaseResponse deleteSite(@PathVariable("siteId") String siteId) {
        return adminService.deleteSite(getCurrentUserId(), siteId);
    }

    /**
     * 세대 추가 등록
     * POST /admin/site/{siteId}/household
     */
    @PostMapping("/site/{siteId}/household")
    public BaseResponse addHousehold(@PathVariable("siteId") String siteId, @RequestBody AdminHouseholdReq req) {
        return adminService.addHousehold(getCurrentUserId(), siteId, req);
    }

    /**
     * 세대 삭제
     * DELETE /admin/site/{siteId}/household/{householdId}
     */
    @DeleteMapping("/site/{siteId}/household/{householdId}")
    public BaseResponse deleteHousehold(@PathVariable("siteId") String siteId, @PathVariable("householdId") String householdId) {
        return adminService.deleteHousehold(getCurrentUserId(), siteId, householdId);
    }

    // ── [3. 소방관할 및 관할배정] ──────────────────────────────────

    /**
     * 전체 소방관할 목록 조회
     * GET /admin/region/fire-regions
     */
    @GetMapping("/region/fire-regions")
    public BaseResponse getFireRegions() {
        return adminService.getFireRegions(getCurrentUserId());
    }

    /**
     * 특정 지역(시도, 시군구) 기반 담당 작업자 목록 조회
     * GET /admin/region/workers
     */
    @GetMapping("/region/workers")
    public BaseResponse getRegionWorkers(
            @RequestParam(value = "sido", required = false) String sido,
            @RequestParam(value = "sigungu", required = false) String sigungu,
            @RequestParam(value = "regionId", required = false) String regionId) {
        return adminService.getRegionWorkers(getCurrentUserId(), sido, sigungu, regionId);
    }

    /**
     * 특정 작업자의 배정 관할 목록 조회
     * GET /admin/user/{userId}/regions
     */
    @GetMapping("/user/{userId}/regions")
    public BaseResponse getUserAssignedRegions(@PathVariable("userId") String targetUserId) {
        return adminService.getUserAssignedRegions(getCurrentUserId(), targetUserId);
    }

    /**
     * 작업자에게 소방관할 배정
     * POST /admin/user/{userId}/regions
     */
    @PostMapping("/user/{userId}/regions")
    public BaseResponse assignRegion(@PathVariable("userId") String targetUserId, @RequestBody Map<String, String> body) {
        String sidoName = body.get("sidoName");
        String regionName = body.get("regionName");
        return adminService.assignRegion(getCurrentUserId(), targetUserId, sidoName, regionName);
    }

    /**
     * 작업자 소방관할 배정 해제
     * DELETE /admin/user/{userId}/regions/{regionId}
     */
    @DeleteMapping("/user/{userId}/regions/{regionId}")
    public BaseResponse unassignRegion(@PathVariable("userId") String targetUserId, @PathVariable("regionId") String regionId) {
        return adminService.unassignRegion(getCurrentUserId(), targetUserId, regionId);
    }

    // ── [4. 문의 관리] ──────────────────────────────────────────

    /**
     * 문의 목록 전체 조회
     * GET /admin/inquiry/list
     */
    @GetMapping("/inquiry/list")
    public BaseResponse getInquiryList() {
        return adminService.getInquiryList(getCurrentUserId());
    }

    /**
     * 답변 대기 문의 요약 (대시보드 전용 경량 조회: 대기 건수 및 최신 1건)
     * GET /admin/inquiry/pending-summary
     */
    @GetMapping("/inquiry/pending-summary")
    public BaseResponse getPendingInquirySummary() {
        return adminService.getPendingInquirySummary(getCurrentUserId());
    }

    /**
     * 문의 단건 상세 조회
     * GET /admin/inquiry/{inquiryId}
     */
    @GetMapping("/inquiry/{inquiryId}")
    public BaseResponse getInquiryDetail(@PathVariable("inquiryId") String inquiryId) {
        return adminService.getInquiryDetail(getCurrentUserId(), inquiryId);
    }

    /**
     * 문의 답변 등록 및 상태 변경
     * PUT /admin/inquiry/{inquiryId}/answer
     */
    @PutMapping("/inquiry/{inquiryId}/answer")
    public BaseResponse answerInquiry(@PathVariable("inquiryId") String inquiryId, @RequestBody AdminInquiryAnswerReq req) {
        return adminService.answerInquiry(getCurrentUserId(), inquiryId, req);
    }

    /**
     * 문의 삭제 (소프트 삭제)
     * DELETE /admin/inquiry/{inquiryId}
     */
    @DeleteMapping("/inquiry/{inquiryId}")
    public BaseResponse deleteInquiry(@PathVariable("inquiryId") String inquiryId) {
        return adminService.deleteInquiry(getCurrentUserId(), inquiryId);
    }
}
