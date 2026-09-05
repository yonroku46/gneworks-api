package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserRes extends ResponseData {
    private String userId;
    private String userName;
    private String phoneNum;
    private String profileImg;
    private Integer roleId;
    private String birthday;
    private String gender;
    private String postalCode;
    private String detailAddress;
    private String lastUpdated;
    private String createTime;
}