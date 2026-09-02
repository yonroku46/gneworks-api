package com.gneworks.api.controller;

import com.gneworks.api.controller.base.BaseController;
import com.gneworks.api.service.UserService;
import com.gneworks.dto.res.core.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public BaseResponse getProfile() {
        return userService.getProfile(getCurrentUserId());
    }

    @PatchMapping("/profile")
    public BaseResponse updateProfile(@RequestBody Map<String, Object> updates) {
        return userService.updateProfile(getCurrentUserId(), updates);
    }
}
