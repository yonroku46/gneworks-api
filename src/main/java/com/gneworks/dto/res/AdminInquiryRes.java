package com.gneworks.dto.res;

import com.gneworks.common.annotation.ExcelColumn;
import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminInquiryRes extends ResponseData {
    private String inquiryId;

    @ExcelColumn(header = "접수일시", order = 1, width = 18, align = "CENTER")
    private String createTime;

    @ExcelColumn(header = "문의유형", order = 2, width = 14, align = "CENTER")
    private String inquiryType;

    @ExcelColumn(header = "작성자", order = 3, width = 14)
    private String userName;

    @ExcelColumn(header = "아이디", order = 4, width = 16, align = "CENTER")
    private String userId;

    @ExcelColumn(header = "전화번호", order = 5, width = 16, align = "CENTER")
    private String phoneNum;

    @ExcelColumn(header = "문의내용", order = 6, width = 45)
    private String inquiryContents;

    @ExcelColumn(header = "처리여부", order = 7, width = 12, align = "CENTER")
    private String statusText; // "답변완료" or "답변대기"

    @ExcelColumn(header = "답변자", order = 8, width = 14)
    private String answerUserName;

    @ExcelColumn(header = "답변일시", order = 9, width = 18, align = "CENTER")
    private String answerTime;

    @ExcelColumn(header = "답변내용", order = 10, width = 45)
    private String answerContents;

    private Boolean processedFlg;
    private Boolean deleteFlg;

    public String getStatusText() {
        if (statusText != null) return statusText;
        return (processedFlg != null && processedFlg) ? "답변완료" : "답변대기";
    }
}
