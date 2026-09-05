package com.gneworks.dto.req;

import lombok.Data;

@Data
public class AdminInquiryAnswerReq {
    private String answerContents;
    private Boolean processedFlg;
}
