package com.gneworks.dto.req;

import lombok.Data;

@Data
public class AdminReportStatusReq {
    private String status;
    private String fixReason;
}