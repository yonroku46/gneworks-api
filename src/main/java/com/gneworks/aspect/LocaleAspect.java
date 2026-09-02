package com.gneworks.aspect;

import com.gneworks.common.constants.SecurityConst;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Aspect
@Component
public class LocaleAspect {

    @Autowired
    protected HttpServletRequest request;

    public static Locale LOCALE;

    @Value("${app.language.default}")
    private String SYSTEM_LANGUAGE_DEFAULT;

    @Before("execution(* com.gneworks.api.controller.*.*(..))")
    public void getLocale() {
        String language = request.getHeader(SecurityConst.LANGUAGE);
        LOCALE = SYSTEM_LANGUAGE_DEFAULT.equalsIgnoreCase(language) ? Locale.KOREA : Locale.ENGLISH;
    }
}
