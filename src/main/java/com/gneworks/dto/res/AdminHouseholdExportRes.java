package com.gneworks.dto.res;

import com.gneworks.common.annotation.ExcelColumn;
import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminHouseholdExportRes extends ResponseData {

    private String householdId;
    private String siteId;

    @ExcelColumn(header = "소방관할", order = 1, width = 16, align = "CENTER")
    private String region;

    @ExcelColumn(header = "시·도", order = 2, width = 12, align = "CENTER")
    private String sido;

    @ExcelColumn(header = "시·군·구", order = 3, width = 16, align = "CENTER")
    private String sigungu;

    @ExcelColumn(header = "읍·면·동", order = 4, width = 14, align = "CENTER")
    private String eupmyeondong;

    @ExcelColumn(header = "도로명 주소", order = 5, width = 35)
    private String address;

    @ExcelColumn(header = "현장명(아파트)", order = 6, width = 28)
    private String siteName;

    @ExcelColumn(header = "동", order = 7, width = 10, align = "CENTER")
    private String dong;

    @ExcelColumn(header = "호수", order = 8, width = 10, align = "CENTER")
    private String ho;

    @ExcelColumn(header = "세대주 성명", order = 9, width = 14, align = "CENTER")
    private String headName;

    @ExcelColumn(header = "보급 대상 유형", order = 10, width = 16, align = "CENTER")
    private String targetType;

    @ExcelColumn(header = "설치 상태", order = 11, width = 14, align = "CENTER")
    private String installStatus;

    @ExcelColumn(header = "보고서 상태", order = 12, width = 14, align = "CENTER")
    private String reportStatus;

    @ExcelColumn(header = "작업자(설치자)", order = 13, width = 14, align = "CENTER")
    private String reporterName;

    @ExcelColumn(header = "보고일시", order = 14, width = 18, align = "CENTER")
    private String reportTime;

    @ExcelColumn(header = "비고", order = 15, width = 30)
    private String remarks;
}
