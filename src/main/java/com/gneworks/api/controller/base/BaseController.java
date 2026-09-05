package com.gneworks.api.controller.base;

import com.gneworks.common.constants.SecurityConst;
import com.gneworks.common.utils.JwtUtils;
import com.gneworks.dao.UserDao;
import com.gneworks.dao.entity.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class BaseController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    UserDao userDao;

    public User loadUser() {
        // 1. Authorization 헤더 우선 확인
        String authorization = request.getHeader(SecurityConst.TOKEN_HEADER);
        if (authorization == null || authorization.trim().isEmpty()) {
            // 2. RefreshToken 헤더 확인
            authorization = request.getHeader(SecurityConst.REFRESH_TOKEN_HEADER);
        }

        if (authorization != null) {
            try {
                String token = authorization.replace(SecurityConst.TOKEN_PREFIX, "").trim();
                Claims claims = JwtUtils.parseJWT(token);
                Object userIdObj = claims.get("userId");
                if (userIdObj != null) {
                    User found = userDao.findUser(userIdObj.toString());
                    if (found != null) {
                        return found;
                    }
                }
            } catch (Exception e) {
                // 토큰 파싱 실패 시 빈 유저 반환
            }
        }
        return new User();
    }

    /**
     * 로그인 중인 유저 이름
     *
     * @return
     */
    public String getCurrentUserName() {
        User entity = loadUser();
        return entity.getUserName();
    }

    /**
     * 로그인 중인 유저 ID
     *
     * @return
     */
    public String getCurrentUserId() {
        User entity = loadUser();
        return entity.getUserId();
    }
}