package com.gneworks.api.service;

import com.gneworks.api.controller.base.BaseController;
import com.gneworks.aspect.LocaleAspect;
import com.gneworks.common.constants.MessageIdConst;
import com.gneworks.common.constants.SecurityConst;
import com.gneworks.common.enums.Roles;
import com.gneworks.common.utils.JwtUtils;
import com.gneworks.common.utils.ResponseUtils;
import com.gneworks.config.SecurityConfig;
import com.gneworks.dao.UserDao;
import com.gneworks.dao.entity.User;
import com.gneworks.dto.res.LoginUserRes;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import com.gneworks.exception.ApplicationException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gneworks.common.utils.ResponseUtils.copyObject;

@Service
@Slf4j
public class AuthService extends BaseController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SecurityConfig securityConfig;

    @Transactional
    public BaseResponse login(String userId, String password) {
        try {
            LoginUserRes res = new LoginUserRes();

            List<User> recordList = userDao.findLoginUser(userId);
            User user = null;
            if (recordList.isEmpty()) {
                return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.I_LOGIN,
                        messageSource.getMessage(MessageIdConst.I_GETTING_FAILED, new String[]{"Login"}, LocaleAspect.LOCALE)), null);
            } else {
                for (User record : recordList) {
                    if (securityConfig.passwordEncoder().matches(password, record.getUserPw())) {
                        user = record;
                        break;
                    }
                }
            }

            if (user == null) {
                return ResponseUtils.generateDtoFailed(new Information(MessageIdConst.I_LOGIN,
                        messageSource.getMessage(MessageIdConst.I_GETTING_FAILED, new String[]{"Login"}, LocaleAspect.LOCALE)), null);
            }

            String token = JwtUtils.createJWT(SecurityConst.EXPIRATION_TIME, user.getUserId());
            String refreshToken = JwtUtils.createJWT(SecurityConst.REFRESH_EXPIRATION_TIME, user.getUserId());
            
            userDao.updateLastLogin(user.getUserId());
            
            res = copyObject(user, res);
            res.setToken(token);
            res.setRefreshToken(refreshToken);

            // 관리자 여부 체크 및 바인딩
            int roleId = user.getRoleId();
            if (roleId == Roles.ROOT.getValue()) {
                res.setMngFlg(Boolean.TRUE);
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_LOGIN,
                    messageSource.getMessage(MessageIdConst.I_GETTING_SUCCESS, new String[]{"Login"}, LocaleAspect.LOCALE)), res);

        } catch (Exception exception) {
            log.error("Login error", exception);
            String message = messageSource.getMessage(MessageIdConst.E_LOGIN_USER_FAILED, null, LocaleAspect.LOCALE);
            throw new ApplicationException(HttpStatus.OK, MessageIdConst.E_LOGIN_USER_FAILED, message);
        }
    }

    public BaseResponse logout() {
        SecurityContextHolder.clearContext();
        return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_LOGOUT,
                messageSource.getMessage(MessageIdConst.I_LOGOUT, null, LocaleAspect.LOCALE)), null);
    }

    @Transactional
    public BaseResponse refreshToken(String refreshToken) {
        try {
            Claims claims = JwtUtils.parseJWT(refreshToken);
            String userId = claims.get("userId", String.class);

            User user = userDao.findUser(userId);
            if (user != null) {
                String newToken = JwtUtils.createJWT(SecurityConst.EXPIRATION_TIME, user.getUserId());
                String newRefreshToken = JwtUtils.createJWT(SecurityConst.REFRESH_EXPIRATION_TIME, user.getUserId());

                LoginUserRes res = new LoginUserRes();
                res = copyObject(user, res);
                res.setToken(newToken);
                res.setRefreshToken(newRefreshToken);

                // 관리자 여부 체크 및 바인딩
                int roleId = user.getRoleId();
                if (roleId == Roles.ROOT.getValue()) {
                    res.setMngFlg(Boolean.TRUE);
                }

                return ResponseUtils.generateDtoSuccess(new Information(MessageIdConst.I_REFRESH_TOKEN, "Token refreshed"), res);
            }
            throw new ApplicationException(HttpStatus.OK, MessageIdConst.E_INVALID_TOKEN, "User not found");
        } catch (Exception e) {
            throw new ApplicationException(HttpStatus.OK, MessageIdConst.E_EXPIRED_TOKEN, "Token expired or invalid");
        }
    }
}