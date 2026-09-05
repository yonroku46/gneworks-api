package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminInquiryRes extends ResponseData {
    private String inquiryId;
    private String userId;
    private String userName;
    private String phoneNum;
    private String inquiryType;
    private String inquiryContents;
    private String answerContents;
    private String answerUserName;
    private String createTime;
    private String answerTime;
    private Boolean processedFlg;
    private Boolean deleteFlg;
}
