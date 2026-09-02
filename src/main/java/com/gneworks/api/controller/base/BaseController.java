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
        User entity = new User();
        String authorization = request.getHeader(SecurityConst.REFRESH_TOKEN_HEADER);
        if (authorization != null) {
            String token = authorization.replace(SecurityConst.TOKEN_PREFIX, "");
            Claims claims = JwtUtils.parseJWT(token);
            Object userIdObj = claims.get("userId");
            if (userIdObj != null) {
                entity = userDao.findUser(userIdObj.toString());
            }
        }
        return entity;
    }

    /**
     * 로그인 중인 사용자명
     *
     * @return
     */
    public String getCurrentUserName() {
        User entity = loadUser();
        return entity.getUserName();
    }

    /**
     * 로그인 중인 사용자 ID
     *
     * @return
     */
    public String getCurrentUserId() {
        User entity = loadUser();
        return entity.getUserId();
    }
}