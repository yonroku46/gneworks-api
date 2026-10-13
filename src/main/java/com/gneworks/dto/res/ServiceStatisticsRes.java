package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStatisticsRes extends ResponseData {
    private String yearMonth;
    private String baseDate;
    private int totalRegionCount;
    private Summary summary;
    private List<RegionMonthlyStatItem> list;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private long totalSites;
        private long totalTarget;
        private long completedTarget;
        private int overallProgressRate;
        private long totalReports;
        private long completedReports;
        private long pendingReports;
        private long refusedReports;
        private long issueReportsCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionMonthlyStatItem {
        private String regionId;
        private String sidoName;
        private String regionName;
        private String displayName;
        private int totalSites;
        private int totalTarget;
        private int completedTarget;
        private int progressRate;
        private int totalReports;
        private int completedReports;
        private int pendingReports;
        private int refusedReports;
        private int issueReportsCount;
    }
}
