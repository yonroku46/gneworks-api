package com.gneworks.api.service;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.utils.ResponseUtils;
import com.gneworks.dao.UserDao;
import com.gneworks.dao.entity.User;
import com.gneworks.dto.res.ActionRes;
import com.gneworks.dto.res.UserRes;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.amazonaws.services.s3.AmazonS3;
import com.gneworks.common.utils.S3Utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.prefix.user}")
    private String userPrefix;

    @Transactional(readOnly = true)
    public BaseResponse getProfile(String userId) {
        User user = userDao.findUser(userId);
        if (user == null) {
            return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.E_USER_NOT_FOUND,
                    messageSource.getMessage(MessageIdConst.E_USER_NOT_FOUND, null, LocaleAspect.LOCALE)));
        }

        // 경로 호환성 처리 (슬러시가 없는 경우 추가)
        if (user.getProfileImg() != null && !user.getProfileImg().startsWith("/") && !user.getProfileImg().startsWith("http")) {
            user.setProfileImg("/" + user.getProfileImg());
        }

        UserRes res = new UserRes();
        BeanUtils.copyProperties(user, res);

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_GETTING_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_GETTING_SUCCESS, new String[] { "User" }, LocaleAspect.LOCALE)), res);
    }

    @Transactional
    public BaseResponse updateProfile(String userId, Map<String, Object> updates) {
        ActionRes res = new ActionRes();
        User user = userDao.findUser(userId);
        if (user == null) {
            return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.E_USER_NOT_FOUND,
                    messageSource.getMessage(MessageIdConst.E_USER_NOT_FOUND, null, LocaleAspect.LOCALE)));
        }

        if (updates.containsKey("userName"))
            user.setUserName((String) updates.get("userName"));
        if (updates.containsKey("phoneNum"))
            user.setPhoneNum((String) updates.get("phoneNum"));
        if (updates.containsKey("gender"))
            user.setGender((String) updates.get("gender"));

        String oldProfileImgToDelete = null;

        if (updates.containsKey("profileImg")) {
            String profileImg = (String) updates.get("profileImg");
            if (profileImg != null && profileImg.startsWith("data:image")) {
                try {
                    String[] parts = profileImg.split(",");
                    String header = parts[0];
                    String base64Data = parts[1];
                    String contentType = header.substring(header.indexOf(":") + 1, header.indexOf(";"));
                    String extension = contentType.contains("/") ? contentType.split("/")[1] : "webp";
                    byte[] bytes = Base64.getDecoder().decode(base64Data);

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String separator = userPrefix.endsWith("/") ? "" : "/";
                    String fileName = userPrefix + separator + userId + "_" + timeStamp + "." + extension;
                    String s3Path = "/" + S3Utils.uploadFile(fileName, bytes, contentType, amazonS3);

                    // 새 사진 업로드 성공 시, 이전 S3 사진을 삭제 대상으로 예약
                    if (user.getProfileImg() != null && !user.getProfileImg().trim().isEmpty() && !user.getProfileImg().startsWith("data:image")) {
                        oldProfileImgToDelete = user.getProfileImg();
                    }
                    user.setProfileImg(s3Path);
                } catch (Exception e) {
                    log.error("Failed to upload profile image to S3", e);
                }
            } else {
                if (profileImg == null || profileImg.trim().isEmpty()) {
                    if (user.getProfileImg() != null && !user.getProfileImg().trim().isEmpty() && !user.getProfileImg().startsWith("data:image")) {
                        oldProfileImgToDelete = user.getProfileImg();
                    }
                }
                user.setProfileImg(profileImg);
            }
        }

        userDao.updateProfile(user);

        // DB 저장까지 성공한 후 이전 프로필 사진을 안전하게 S3에서 삭제
        if (oldProfileImgToDelete != null) {
            try {
                String oldS3Key = oldProfileImgToDelete.startsWith("/") ? oldProfileImgToDelete.substring(1) : oldProfileImgToDelete;
                S3Utils.deleteFile(oldS3Key, amazonS3);
            } catch (Exception ex) {
                log.warn("Failed to delete old profile image from S3: {}", oldProfileImgToDelete, ex);
            }
        }
        res.setSuccess(Boolean.TRUE);

        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_UPDATE_SUCCESS,
                messageSource.getMessage(MessageIdConst.I_UPDATE_SUCCESS, new String[] { "User" }, LocaleAspect.LOCALE)), res);
    }
}
