package com.gneworks.api.service;

import com.gneworks.common.id.KsuidGenerator;
import com.gneworks.dao.SiteDao;
import com.gneworks.dao.WorkReportDao;
import com.gneworks.dao.entity.Household;
import com.gneworks.dao.entity.WorkReport;
import com.gneworks.dto.req.WorkReportReq;
import com.gneworks.dto.res.WorkReportRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class WorkReportTxService {

    @Autowired
    private WorkReportDao workReportDao;

    @Autowired
    private SiteDao siteDao;

    /**
     * 시공 보고서 DB 저장 및 갱신 (순수 DB 작업만 초단기 트랜잭션으로 격리)
     */
    @Transactional
    public WorkReportRes saveReportTransaction(
            String userId,
            WorkReportReq req,
            String confirmerSignature,
            String photoDoor,
            String photoBefore1,
            String photoAfter1,
            String photoBefore2,
            String photoAfter2,
            Date installDate) {

        String householdId = req.getHouseholdId().trim();
        WorkReport existing = workReportDao.selectByHouseholdId(householdId);

        // 다른 작업자가 이미 작성한 보고서인 경우 차단
        if (existing != null && existing.getUserId() != null && !existing.getUserId().trim().isEmpty()) {
            if (!existing.getUserId().trim().equals(userId.trim())) {
                throw new IllegalStateException("ACCESS_DENIED");
            }
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
            return workReportDao.selectReportDetailById(report.getReportId());
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

            return workReportDao.selectReportDetailById(existing.getReportId());
        }
    }
}
