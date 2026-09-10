package com.gneworks.dto.req;

import lombok.Data;

@Data
public class SystemSettingsReq {
    private String contactPhone;
    private String contactEmail;
    private Boolean noticeVisible;
    private String noticeTitle;
    private String noticeContent;
    private String noticeDate;
}
