package com.gneworks.dto.req;

import lombok.Data;

@Data
public class AdminReportSearchReq {
    private String regionId;
    private String status;
    private String installStartDate;
    private String installEndDate;
    private String reportStartDate;
    private String reportEndDate;
    private String query;
    private String userId;
    private String siteId;
    private Integer limit;
    private String orderBy;
    private Boolean hasRemarks;

    private String portalUserId;
    private java.util.List<String> assignedRegionIds;

    private Integer page;
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public int getOffset() {
        int p = (page != null && page > 0) ? page : 1;
        int s = (size != null && size > 0) ? size : 30;
        return (p - 1) * s;
    }
}