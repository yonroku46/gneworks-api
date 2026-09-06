package com.gneworks.api.controller;

import com.gneworks.api.controller.base.BaseController;
import com.gneworks.api.service.ContactService;
import com.gneworks.dto.req.InquiryReq;
import com.gneworks.dto.res.core.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
@Slf4j
public class ContactController extends BaseController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/submit")
    public BaseResponse submitInquiry(@RequestBody InquiryReq req) {
        String userId = getCurrentUserId();
        return contactService.sendInquiry(userId, req);
    }
}
