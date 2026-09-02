package com.gneworks.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 문자열 조작 관련 유틸리티
 *
 * @author y_ha
 */
@Component
public class StringUtils {

    /**
     * 인스턴스 생성 제어 (서브클래스만 허용)
     */
    protected StringUtils() {
    }

    /**
     * ObjectMapper의 static 변수
     */
    private static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 빈 문자열인지 판단
     *
     * @param val 대상 문자열
     * @return true:빈 문자열 / false:빈 문자열 아님
     */
    public static boolean isEmpty(String val) {
        return val == null || val.length() == 0;
    }

    /**
     * 빈 문자열이 아닌지 판단
     *
     * @param val 대상 문자열
     * @return true:빈 문자열 아님 / false:빈 문자열
     */
    public static boolean isNotEmpty(String val) {
        return !isEmpty(val);
    }

    /**
     * 인터페이스 에러 메시지 변환 (파라미터 없음)
     *
     * @param methodName 메서드명
     * @param exception 발생 원인
     * @return 변환 문자열
     */
    public static String convertInterfaceErrorMsg(String methodName, Exception exception) {
        return convertInterfaceErrorMsg(methodName, null, exception);
    }

    /**
     * 인터페이스 에러 메시지 변환 (파라미터 있음)
     *
     * @param methodName 메서드명
     * @param param 파라미터 (Dto 또는 HashMap)
     * @param exception 발생 원인
     * @return 변환 문자열
     */
    public static String convertInterfaceErrorMsg(String methodName, Object param, Exception exception) {
        StringBuilder builder = new StringBuilder();
        builder.append("method name=");
        builder.append(methodName);
        builder.append(" param=");
        if (param != null) {
            // 파라미터를 JSON 문자열로 변환하여 설정
            builder.append(StringUtils.convertObjectToJsonString(param));
        } else {
            builder.append("none");
        }
        builder.append(" error detail=");
        // 스택 트레이스를 문자열로 변환하여 설정
        builder.append(convertStackTraceToString(exception));
        return builder.toString();
    }

    /**
     * 스택 트레이스를 문자열로 변환
     *
     * @param exception 예외 클래스
     * @return 스택 트레이스 문자열
     */
    public static String convertStackTraceToString(Exception exception) {
        String stackTraceStr = null;

        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        stackTraceStr = writer.toString();

        // GC용 null 처리
        writer = null;

        return stackTraceStr;

    }

    /**
     * Object 타입을 JSON 문자열로 변환
     *
     * @param object 객체
     * @return
     */
    public static String convertObjectToJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * JSON 문자열을 MAP 타입으로 변환
     *
     * @param json JSON 문자열
     * @return
     */
    public static Map<String, Object> jsonStringToMap(String json) {
        Map<String, Object> resultMap = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            resultMap = gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 문자열을 List<String> 타입으로 변환
     *
     * @param str Array 문자열
     * @return
     */
    public static List<String> stringToStringList(String str) {
        List<String> resultList = new ArrayList<>();
        try {
            Gson gson = new Gson();
            resultList = gson.fromJson(str, new TypeToken<List<String>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * 문자열을 List<Integer> 타입으로 변환
     *
     * @param str Array 문자열
     * @return
     */
    public static List<Integer> stringToIntegerList(String str) {
        List<Integer> resultList = new ArrayList<>();
        try {
            Gson gson = new Gson();
            List<String> parsedList = gson.fromJson(str, new TypeToken<List<String>>() {}.getType());
            for (String element : parsedList) {
                if (isNumeric(element)) {
                    Integer number = Integer.parseInt(element);
                    resultList.add(number);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * 문자열이 숫자인지 판단
     *
     * @param str 문자열
     * @return
     */
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * 문자열이 비어있거나 null인지 판별
     *
     * @param str 판별 대상 문자열
     * @return 비어있거나 null인 경우 true, 그 외에는 false
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 생년월일을 현재 나이로 변환
     *
     * @param birthday 문자열
     * @return
     */
    public static int calculateAge(String birthday) {
        // 문자열을 LocalDate 객체로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birthday, formatter);
        LocalDate currentDate = LocalDate.now();

        // 현재 날짜와 생년월일 날짜의 차이를 계산
        int age = Period.between(birthDate, currentDate).getYears();

        return age;
    }
}
