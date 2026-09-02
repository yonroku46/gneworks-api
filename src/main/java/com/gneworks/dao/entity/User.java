package com.gneworks.dao.entity;

import jakarta.annotation.Generated;
import java.util.Date;

public class User {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.user_id, Type: CHAR(27), Remark: 사용자 키")
    private String userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.user_name, Type: VARCHAR(50), Remark: 사용자명")
    private String userName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.user_pw, Type: VARCHAR(500), Remark: 비밀번호")
    private String userPw;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.phone_num, Type: VARCHAR(15), Remark: 전화번호")
    private String phoneNum;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.profile_img, Type: VARCHAR(300), Remark: 프로필 이미지")
    private String profileImg;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.role_id, Type: INT, Remark: 역할 키")
    private Integer roleId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.birthday, Type: DATE, Remark: 생년월일")
    private Date birthday;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.gender, Type: VARCHAR(5), Remark: 성별")
    private String gender;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.postal_code, Type: VARCHAR(10), Remark: 우편번호")
    private String postalCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.detail_address, Type: VARCHAR(200), Remark: 주소")
    private String detailAddress;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.last_update, Type: DATETIME, Default value: CURRENT_TIMESTAMP, Remark: 최종 로그인 일시")
    private Date lastUpdate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: user.delete_flg, Type: TINYINT(3), Default value: 0, Remark: 삭제 여부")
    private Byte deleteFlg;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getUserId() {
        return userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
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
    public String getUserPw() {
        return userPw;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserPw(String userPw) {
        this.userPw = userPw == null ? null : userPw.trim();
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
    public String getProfileImg() {
        return profileImg;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg == null ? null : profileImg.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getRoleId() {
        return roleId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getBirthday() {
        return birthday;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getGender() {
        return gender;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPostalCode() {
        return postalCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode == null ? null : postalCode.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getDetailAddress() {
        return detailAddress;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress == null ? null : detailAddress.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getDeleteFlg() {
        return deleteFlg;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDeleteFlg(Byte deleteFlg) {
        this.deleteFlg = deleteFlg;
    }
}