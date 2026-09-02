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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class ContactService {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private InquiryDao inquiryDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    public BaseResponse sendInquiry(String userId, InquiryReq req) {
        ActionRes res = new ActionRes();

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

        res.setSuccess(Boolean.TRUE);
        res.setId(inquiryId);

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_INSERT_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_INSERT_SUCCESS, new String[] { "Inquiry" }, LocaleAspect.LOCALE)), res);
    }
}