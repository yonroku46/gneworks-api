package com.gneworks.api.service;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.id.KsuidGenerator;
import com.gneworks.common.utils.ResponseUtils;
import com.gneworks.common.utils.StringUtils;
import com.gneworks.dao.InquiryDao;
import com.gneworks.dao.UserDao;
import com.gneworks.dao.entity.Inquiry;
import com.gneworks.dao.entity.User;
import com.gneworks.dto.req.InquiryReq;
import com.gneworks.dto.res.ActionRes;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import com.gneworks.dto.res.ListRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContactService {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private InquiryDao inquiryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AppNotificationService appNotificationService;

    @Transactional
    public BaseResponse sendInquiry(String userId, InquiryReq req) {
        ActionRes res = new ActionRes();

        // 허니팟 필드에 값이 채워져 있으면 자동화된 스팸 봇으로 간주
        if (req.getWebsite() != null && !req.getWebsite().trim().isEmpty()) {
            log.warn("[BOT_DETECTED] Honeypot triggered in inquiry submission. Discarding silently. Value: '{}'", req.getWebsite());
            res.setSuccess(Boolean.TRUE);
            res.setId("BOT_" + KsuidGenerator.createId());
            return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_INSERT_SUCCESS,
                    messageSource.getMessage(MessageIdConst.I_INSERT_SUCCESS, new String[] { "Inquiry" }, LocaleAspect.LOCALE)), res);
        }

        Inquiry inquiry = new Inquiry();
        String inquiryId = KsuidGenerator.createId();
        inquiry.setInquiryId(inquiryId);

        // 로그인 사용자 또는 요청에 포함된 사용자 ID 확인
        String targetUserId = !StringUtils.isBlank(userId) ? userId : req.getUserId();
        if (!StringUtils.isBlank(targetUserId)) {
            User user = userDao.findUser(targetUserId.trim());
            if (user != null) {
                inquiry.setUserId(user.getUserId());
                inquiry.setUserName(user.getUserName());
            }
        }

        inquiry.setInquiryType(req.getInquiryType());
        inquiry.setPhoneNum(req.getPhoneNum());
        inquiry.setInquiryContents(req.getInquiryContents());

        inquiryDao.saveInquiry(inquiry);

        // 관리자 전원에게 실시간 SSE 및 웹 푸시 알림 발송
        try {
            String inqType = req.getInquiryType() != null && !req.getInquiryType().isBlank() ? req.getInquiryType() : "업무 문의";
            String title = "신규 문의 접수";
            String message = String.format("[%s] 신규 문의사항이 접수되었습니다.", inqType);
            appNotificationService.sendNotificationToAdmins(title, message, "/manage/inquiries", "LOGO");
        } catch (Exception e) {
            log.error("Failed to notify admins of new inquiry: {}", e.getMessage());
        }

        res.setSuccess(Boolean.TRUE);
        res.setId(inquiryId);

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_INSERT_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_INSERT_SUCCESS, new String[] { "Inquiry" }, LocaleAspect.LOCALE)), res);
    }
}