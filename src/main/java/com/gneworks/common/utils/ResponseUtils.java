package com.gneworks.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.gneworks.common.enums.Result;
import com.gneworks.dto.res.core.BaseResponse;
import com.gneworks.dto.res.core.Information;
import com.gneworks.dto.res.core.ResponseData;
import com.gneworks.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 응답 반환 관련 유틸리티
 *
 * @author y_ha
 */
@Component
public class ResponseUtils {

    /**
     * 인스턴스 생성 제어 (서브클래스만 허용)
     */
    public ResponseUtils() {
    }

    /**
     * 클라이언트 에러가 발생했는지 체크
     *
     * @param exception
     */
    public static void isClientError(ApplicationException exception) {
        if (exception.getStatus() == null) {
            return;
        }
        // 200 이외의 경우 강제적으로 예외 던짐
        if (!HttpStatus.OK.equals(exception.getStatus())) {
            throw exception;
        }
    }

    /**
     * 200 : 정상 응답 생성 (단일 메시지)
     *
     * @param information 메시지 정보
     * @param responseData
     * @return
     */
    public static BaseResponse generateDtoSuccess(Information information, ResponseData responseData) {
        return generateDto(Result.SUCCESS, false, information, null, responseData);
    }

    /**
     * 200 : 정상 응답 생성 (다중 메시지)
     *
     * @param informations 메시지 정보 목록
     * @param responseData
     * @return
     */
    public static BaseResponse generateDtoSuccess(List<Information> informations, ResponseData responseData) {
        return generateDto(Result.SUCCESS, false, informations, null, responseData);
    }

    /**
     * 200 : 이상 발생 응답 생성
     *
     * @param information
     * @param responseData
     * @return
     */
    public static BaseResponse generateDtoSuccessAbnormal(Information information, ResponseData responseData) {
        return generateDto(Result.SUCCESS, true, information, null, responseData);
    }

    public static BaseResponse generateDtoFailed(Information information) {
        return generateDto(Result.FAILED, true, information, null, null);
    }

    public static BaseResponse generateDtoFailed(Error error) {
        return generateDto(Result.FAILED, true, null, error, null);
    }

    public static BaseResponse generateDtoFailed(List<Error> errors) {
        return generateDto(Result.FAILED, true, null, errors, null);
    }

    public static BaseResponse generateDtoFailed(Information information, Error error) {
        return generateDto(Result.FAILED, true, information, error, null);
    }

    public static BaseResponse generateDtoFailed(List<Information> informations, List<Error> errors) {
        return generateDto(Result.FAILED, true, informations, errors, null);
    }

    public static BaseResponse generateDtoFailed(Information information, Error error, ResponseData responseData) {
        return generateDto(Result.FAILED, true, information, error, responseData);
    }

    public static BaseResponse generateDtoFailed(List<Information> informations, List<Error> errors, ResponseData responseData) {
        return generateDto(Result.FAILED, true, informations, errors, responseData);
    }

    public static BaseResponse generateDto(Result result, boolean hasErrors, Information information, Error error, ResponseData responseData) {
        List<Information> informations = null;
        if (information != null) {
            informations = new ArrayList<>();
            informations.add(information);
        }

        List<Error> errors = null;
        if (error != null) {
            errors = new ArrayList<>();
            errors.add(error);
        }

        return generateDto(result, hasErrors, informations, errors, responseData);
    }

    /**
     * 응답 DTO 생성
     *
     * @param result
     * @param hasErrors
     * @param informations
     * @param errors
     * @param responseData
     * @return
     */
    public static BaseResponse generateDto(Result result, boolean hasErrors, List<Information> informations, List<Error> errors, ResponseData responseData) {
        BaseResponse dto = new BaseResponse();
        dto.setResultCode(result.getCode());
        dto.setHasErrors(hasErrors);
        dto.setInformations(informations);
        dto.setErrors(errors);
        dto.setResponseData(responseData);

        return dto;
    }

    /**
     * 객체 복사
     *
     * @param sourceObject
     * @param targetObject
     * @return
     */
    public static <A, B> B copyObject(A sourceObject, B targetObject) {
        if (sourceObject == null || targetObject == null) {
            return null;
        }
        try {
            for (Field field : sourceObject.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Field targetField = targetObject.getClass().getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    if (targetField.getType().isAssignableFrom(field.getType())) {
                        targetField.set(targetObject, field.get(sourceObject));
                    }
                } catch (NoSuchFieldException e) {
                    // targetObject에 없는 항목은 무시
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return targetObject;
    }
}
