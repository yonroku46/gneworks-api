package com.gneworks.api.controller;

import com.gneworks.api.controller.base.BaseController;
import com.gneworks.api.service.AuthService;
import com.gneworks.common.enums.AuthType;
import com.gneworks.dto.req.*;
import com.gneworks.dto.res.core.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController extends BaseController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginReq req) {
        return authService.login(req.getUserId(), req.getPassword());
    }

    @PostMapping("/logout")
    public BaseResponse logout() {
        return authService.logout();
    }

    @PostMapping("/refresh")
    public BaseResponse refresh(@RequestBody RefreshReq req) {
        return authService.refreshToken(req.getRefreshToken());
    }
}
