package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminSiteSummaryRes extends ResponseData {
    private long totalSites;
    private long totalDongs;
    private long totalHouseholds;
    private long totalWorkers;
}
