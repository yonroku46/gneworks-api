package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginUserRes extends ResponseData {

    private String userId;

    private String userName;

    private String profileImg;

    private String phoneNum;

    private String gender;

    private String token;

    private String refreshToken;

    private boolean mngFlg;
}
