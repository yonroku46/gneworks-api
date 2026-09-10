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
public class PortalNoticeRes extends ResponseData {
    private Boolean noticeVisible;
    private String noticeTitle;
    private String noticeContent;
    private String noticeDate;
    private String contactPhone;
    private String contactEmail;
}