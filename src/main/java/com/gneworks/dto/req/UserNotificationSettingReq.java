package com.gneworks.dto.req;

import lombok.Data;

@Data
public class UserNotificationSettingReq {
    private Boolean notifyWebPush;
    private Boolean notifyNewReport;
    private Boolean notifyNewInquiry;
    private Boolean notifyReportStatus;
    private Boolean notifyInquiryAnswer;
}