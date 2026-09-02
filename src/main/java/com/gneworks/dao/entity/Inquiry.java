package com.gneworks.dao.entity;

import jakarta.annotation.Generated;
import java.util.Date;

public class Inquiry {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.inquiry_id, Type: CHAR(27), Remark: 문의 키")
    private String inquiryId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.user_id, Type: CHAR(27), Remark: 사용자 키")
    private String userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.inquiry_type, Type: VARCHAR(100), Remark: 문의 유형")
    private String inquiryType;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.phone_num, Type: VARCHAR(15), Remark: 전화번호")
    private String phoneNum;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.user_name, Type: VARCHAR(50), Remark: 작성자명")
    private String userName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.answer_user_name, Type: VARCHAR(50), Remark: 답변자명")
    private String answerUserName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.create_time, Type: DATETIME, Default value: CURRENT_TIMESTAMP, Remark: 생성 일시")
    private Date createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.answer_time, Type: DATETIME, Default value: CURRENT_TIMESTAMP, Remark: 답변 일시")
    private Date answerTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.processed_flg, Type: TINYINT(3), Default value: 0, Remark: 처리 여부")
    private Byte processedFlg;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.delete_flg, Type: TINYINT(3), Default value: 0, Remark: 삭제 여부")
    private Byte deleteFlg;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.inquiry_contents, Type: TEXT, Remark: 문의 내용")
    private String inquiryContents;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: inquiry.answer_contents, Type: TEXT, Remark: 답변 내용")
    private String answerContents;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getInquiryId() {
        return inquiryId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setInquiryId(String inquiryId) {
        this.inquiryId = inquiryId == null ? null : inquiryId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getUserId() {
        return userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getInquiryType() {
        return inquiryType;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setInquiryType(String inquiryType) {
        this.inquiryType = inquiryType == null ? null : inquiryType.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPhoneNum() {
        return phoneNum;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getUserName() {
        return userName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAnswerUserName() {
        return answerUserName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAnswerUserName(String answerUserName) {
        this.answerUserName = answerUserName == null ? null : answerUserName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getAnswerTime() {
        return answerTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getProcessedFlg() {
        return processedFlg;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setProcessedFlg(Byte processedFlg) {
        this.processedFlg = processedFlg;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getDeleteFlg() {
        return deleteFlg;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDeleteFlg(Byte deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getInquiryContents() {
        return inquiryContents;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setInquiryContents(String inquiryContents) {
        this.inquiryContents = inquiryContents == null ? null : inquiryContents.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAnswerContents() {
        return answerContents;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAnswerContents(String answerContents) {
        this.answerContents = answerContents == null ? null : answerContents.trim();
    }
}