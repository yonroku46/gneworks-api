package com.gneworks.exception;

import com.gneworks.common.utils.ResponseUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<BaseResponse> applicationExceptionHandle(ApplicationException exception) {
        return new ResponseEntity<>(
                ResponseUtils.generateDtoSuccessAbnormal(
                        new Information(exception.getErrorCode(), exception.getMessage()), null
                ),
                exception.getStatus()
        );
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<BaseResponse> jsonProcessingExceptionHandle(JsonProcessingException exception) {
        return new ResponseEntity<>(
                ResponseUtils.generateDtoSuccessAbnormal(
                        new Information(null, exception.getMessage()), null
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<BaseResponse> expiredJwtExceptionHandle(ExpiredJwtException exception) {
        return new ResponseEntity<>(
                ResponseUtils.generateDtoSuccessAbnormal(
                        new Information(null, exception.getMessage()), null
                ),
                HttpStatus.UNAUTHORIZED
        );
    }
}
