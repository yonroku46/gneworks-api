package com.gneworks.filter;

import com.gneworks.aspect.LocaleAspect;
import com.gneworks.aspect.attribute.CheckToken;
import com.gneworks.aspect.attribute.LoginToken;
import com.gneworks.common.constants.SecurityConst;
import com.gneworks.common.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.Filter;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class JwtAuthFilter implements Filter {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            if (!preHandle((HttpServletRequest) request, (HttpServletResponse) response)) {
                return;
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod;
        try {
            handlerMethod = (HandlerMethod) handlerMapping.getHandler(request).getHandler();
        } catch (Exception e) {
            return true;
        }
        Class<?> clazz = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken loginToken = method.getAnnotation(LoginToken.class);
            if (loginToken.required()) {
                return true;
            }
        }
        boolean isNeedVerify = false;

        if (clazz.isAnnotationPresent(CheckToken.class)) {
            CheckToken checkControllerToken = clazz.getAnnotation(CheckToken.class);
            if (checkControllerToken.required()) {
                isNeedVerify = true;
            }
        }
        if (method.isAnnotationPresent(CheckToken.class)) {
            CheckToken checkMethodToken = method.getAnnotation(CheckToken.class);
            if (checkMethodToken.required()) {
                isNeedVerify = true;
            }
        }

        if (isNeedVerify) {
            return verifyToken(request, response);
        }
        return true;
    }

    private boolean verifyToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String authorization = httpServletRequest.getHeader(SecurityConst.TOKEN_HEADER);

        if (StringUtils.isBlank(authorization)) {
            String message = messageSource.getMessage("error.noAccessToken", null, LocaleAspect.LOCALE);
            httpServletResponse.sendError((HttpServletResponse.SC_UNAUTHORIZED), message);
            return false;
        }
        String token = authorization.replace(SecurityConst.TOKEN_PREFIX, "");
        if (StringUtils.isBlank(token)) {
            String message = messageSource.getMessage("error.noAccessToken", null, LocaleAspect.LOCALE);
            httpServletResponse.sendError((HttpServletResponse.SC_UNAUTHORIZED), message);
            return false;
        }

        try {
            Claims claims = JwtUtils.parseJWT(token);
            String userId = claims.get("userId", String.class);

            if (userId == null) {
                String message = messageSource.getMessage("error.userNotRegister", null, LocaleAspect.LOCALE);
                httpServletResponse.sendError(HttpServletResponse.SC_CONFLICT, message);
                return false;
            }

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("client");
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (ExpiredJwtException exception) {
            String message = messageSource.getMessage("error.expiredJWT", new String[]{token, exception.getMessage()}, LocaleAspect.LOCALE);
            httpServletResponse.sendError((HttpServletResponse.SC_UNAUTHORIZED), exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            String message = messageSource.getMessage("error.unsupportedJWT", new String[]{token, exception.getMessage()}, LocaleAspect.LOCALE);
            httpServletResponse.sendError((HttpServletResponse.SC_UNAUTHORIZED), exception.getMessage());
        } catch (MalformedJwtException exception) {
            String message = messageSource.getMessage("error.invalidJWT", new String[]{token, exception.getMessage()}, LocaleAspect.LOCALE);
            httpServletResponse.sendError((HttpServletResponse.SC_UNAUTHORIZED), exception.getMessage());
        } catch (IllegalArgumentException exception) {
            String message = messageSource.getMessage("error.illegalJWT", new String[]{token, exception.getMessage()}, LocaleAspect.LOCALE);
            httpServletResponse.sendError((HttpServletResponse.SC_UNAUTHORIZED), exception.getMessage());
        }
        return false;
    }
}