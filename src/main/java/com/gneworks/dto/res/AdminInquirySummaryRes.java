package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminInquirySummaryRes extends ResponseData {
    private int pendingCount;
    private AdminInquiryRes latestPendingInquiry;
}
