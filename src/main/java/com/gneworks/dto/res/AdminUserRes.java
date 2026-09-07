package com.gneworks.dto.res;

import com.gneworks.common.annotation.ExcelColumn;
import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserRes extends ResponseData {
    @ExcelColumn(header = "아이디", order = 1, width = 18, align = "CENTER")
    private String userId;

    @ExcelColumn(header = "사용자명", order = 2, width = 16)
    private String userName;

    @ExcelColumn(header = "전화번호", order = 3, width = 16, align = "CENTER")
    private String phoneNum;

    private String profileImg;
    private Integer roleId;

    @ExcelColumn(header = "성별", order = 4, width = 10, align = "CENTER")
    private String gender;

    @ExcelColumn(header = "생년월일", order = 5, width = 14, align = "CENTER")
    private String birthday;

    @ExcelColumn(header = "우편번호", order = 6, width = 12, align = "CENTER")
    private String postalCode;

    @ExcelColumn(header = "주소", order = 7, width = 35)
    private String detailAddress;

    @ExcelColumn(header = "담당지역 수", order = 8, width = 14, align = "CENTER")
    private Integer regionCount;

    @ExcelColumn(header = "작업 실적(건)", order = 9, width = 14, align = "CENTER")
    private Integer reportCount;

    private String lastUpdated;

    @ExcelColumn(header = "등록일시", order = 10, width = 18, align = "CENTER")
    private String createTime;
}