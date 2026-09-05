package com.gneworks.dto.req;

import lombok.Data;
import java.util.Date;

@Data
public class AdminUserReq {
    private String userId;
    private String userName;
    private String phoneNum;
    private String birthday; // yyyy-MM-dd
    private String gender;
    private String postalCode;
    private String detailAddress;
}