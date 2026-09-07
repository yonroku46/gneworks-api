package com.gneworks.dto.res;

import com.gneworks.common.annotation.ExcelColumn;
import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminSiteRes extends ResponseData {
    private String siteId;
    private String regionId;

    @ExcelColumn(header = "현장명(아파트)", order = 1, width = 25)
    private String name;

    @ExcelColumn(header = "도로명 주소", order = 2, width = 35)
    private String address;

    @ExcelColumn(header = "시·도", order = 3, width = 12, align = "CENTER")
    private String sido;

    @ExcelColumn(header = "시·군·구", order = 4, width = 14, align = "CENTER")
    private String sigungu;

    @ExcelColumn(header = "읍·면·동", order = 5, width = 14, align = "CENTER")
    private String eupmyeondong;

    @ExcelColumn(header = "단지 규모(동)", order = 6, width = 14, align = "RIGHT")
    private int dongCount;

    @ExcelColumn(header = "대상 세대수", order = 7, width = 14, align = "RIGHT")
    private int totalHouseholds;

    @ExcelColumn(header = "설치 완료 세대수", order = 8, width = 16, align = "RIGHT")
    private int completedHouseholds;

    @ExcelColumn(header = "소방관할", order = 9, width = 16, align = "CENTER")
    private String region;

    @ExcelColumn(header = "연락처", order = 10, width = 16, align = "CENTER")
    private String contactPhone;

    @ExcelColumn(header = "등록일자", order = 11, width = 18, align = "CENTER", dateFormat = "yyyy-MM-dd HH:mm")
    private Date createTime;

    private List<HouseholdRes> households;
    private List<RegionWorkerRes> assignedWorkers;
}
