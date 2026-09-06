package com.gneworks.dto.req;

import lombok.Data;

@Data
public class WorkReportReq {
    private String reportId;
    private String householdId;
    private String siteId;
    private String dong;
    private String ho;
    private String headName;
    private String installDate;
    private String reporterName;
    private String confirmerName;
    private String confirmerSignature;
    private String photoDoor;
    private String photoBefore1;
    private String photoAfter1;
    private String photoBefore2;
    private String photoAfter2;
    private String remarks;
    private String status;
}
