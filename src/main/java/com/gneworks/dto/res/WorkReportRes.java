package com.gneworks.dto.res;

import com.gneworks.common.annotation.ExcelColumn;
import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorkReportRes extends ResponseData {
    @ExcelColumn(header = "보고서ID", order = 1, width = 16, align = "CENTER")
    private String reportId;

    private String householdId;
    private String siteId;
    private String regionId;
    private String region;
    private String regionSido;

    @ExcelColumn(header = "현장명(아파트)", order = 2, width = 24)
    private String siteName;

    @ExcelColumn(header = "시도", order = 3, width = 12)
    private String sido;

    @ExcelColumn(header = "시군구", order = 4, width = 14)
    private String sigungu;

    @ExcelColumn(header = "읍면동", order = 5, width = 14)
    private String eupmyeondong;

    @ExcelColumn(header = "상세주소", order = 6, width = 30)
    private String address;

    private String userId;
    private String installerId;

    @ExcelColumn(header = "동", order = 7, width = 10, align = "CENTER")
    private String dong;

    @ExcelColumn(header = "호", order = 8, width = 10, align = "CENTER")
    private String ho;

    @ExcelColumn(header = "세대주명", order = 9, width = 14, align = "CENTER")
    private String headName;

    private String targetType;

    private String installDate;

    @ExcelColumn(header = "설치일자", order = 10, width = 14, align = "CENTER")
    private String installDateFormatted;

    @ExcelColumn(header = "보고일시", order = 11, width = 18, align = "CENTER")
    private String reportTime;

    private String submittedAt;

    @ExcelColumn(header = "보고자(설치자)", order = 12, width = 14, align = "CENTER")
    private String reporterName;

    private String confirmerName;
    private String confirmerSignature;
    private String photoDoor;
    private String photoBefore1;
    private String photoAfter1;
    private String photoBefore2;
    private String photoAfter2;

    @ExcelColumn(header = "상태", order = 13, width = 12, align = "CENTER")
    private String status;

    @ExcelColumn(header = "특이사항", order = 14, width = 30)
    private String remarks;

    @ExcelColumn(header = "수정/반려사유", order = 15, width = 30)
    private String fixReason;

    private Date createTime;
    private Date lastUpdate;
}