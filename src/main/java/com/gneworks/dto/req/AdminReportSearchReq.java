package com.gneworks.dto.req;

import lombok.Data;

@Data
public class AdminReportSearchReq {
    private String sido;
    private String sigungu;
    private String eupmyeondong;
    private String status;
    private String installStartDate;
    private String installEndDate;
    private String reportStartDate;
    private String reportEndDate;
    private String query;
    private String userId;
    private String siteId;
}