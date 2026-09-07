package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminDashboardSummaryRes extends ResponseData {
    private long totalSites;
    private long totalTarget;       // 총 세대수
    private long completedTarget;   // 설치완료 세대수
    private int progressRate;       // 진행률 %
    private long totalReports;      // 누적 보고서
    private long todayReports;      // 오늘 보고서
    private long pendingReports;    // 심사 대기
    private long rejectedReports;   // 반려
    private long completedReports;  // 승인 완료
    private long issueReportsCount; // 특이사항 보고서
    private long totalWorkers;      // 총 작업자 수
}
