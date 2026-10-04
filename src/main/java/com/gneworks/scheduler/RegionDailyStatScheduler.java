package com.gneworks.scheduler;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gneworks.api.service.AdminService;

/**
 * 지역별 일일 통계 배치 스케줄러
 * 매일 밤 12시(00:00:00)에 실행되어 어제 일자 마감 집계 및 오늘 일자 초기 집계를 수행합니다.
 */
@Component
public class RegionDailyStatScheduler {

    private static final Logger log = LoggerFactory.getLogger(RegionDailyStatScheduler.class);

    @Autowired
    private AdminService adminService;

    /**
     * 한국 표준시(KST) 기준 매일 밤 12시 (00:00:00) 실행
     */
    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    public void runMidnightDailyStatBatch() {
        log.info("[Batch Scheduler] Starting midnight daily statistics aggregation batch...");

        try {
            // 1. 어제 날짜(D-1) 최종 마감 스냅샷 생성
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Date yesterday = cal.getTime();
            adminService.generateDailyStats(yesterday);

            // 2. 오늘 날짜(D-day) 시작 스냅샷 생성
            Date today = new Date();
            adminService.generateDailyStats(today);

            log.info("[Batch Scheduler] Midnight daily statistics aggregation batch completed successfully.");
        } catch (Exception e) {
            log.error("[Batch Scheduler] Error during midnight daily statistics aggregation batch", e);
        }
    }
}
