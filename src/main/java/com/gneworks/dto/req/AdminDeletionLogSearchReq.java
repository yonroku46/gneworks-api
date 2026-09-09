package com.gneworks.dto.req;

import lombok.Data;

@Data
public class AdminDeletionLogSearchReq {
    private String regionId;
    private String sido;
    private String sigungu;
    private String query;
    private String startDate;
    private String endDate;

    private Integer page;
    private Integer size;

    public int getOffset() {
        int p = (page != null && page > 0) ? page : 1;
        int s = (size != null && size > 0) ? size : 30;
        return (p - 1) * s;
    }

    public int getLimit() {
        return (size != null && size > 0) ? size : 30;
    }
}