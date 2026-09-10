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
public class SystemSettingsRes extends ResponseData {
    private Byte settingId;
    private String contactPhone;
    private String contactEmail;
    private Boolean noticeVisible;
    private String noticeTitle;
    private String noticeContent;
    private String noticeDate;
}