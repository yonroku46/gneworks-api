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
@EqualsAndHashCode(callSuper = false)
public class UserNotificationSettingRes extends ResponseData {
    private String userId;
    private Boolean notifyWebPush;
    private Boolean notifyNewReport;
    private Boolean notifyNewInquiry;
    private Boolean notifyReportStatus;
    private Boolean notifyInquiryAnswer;
}