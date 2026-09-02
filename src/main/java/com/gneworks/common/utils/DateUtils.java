package com.gneworks.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date 타입 관련 유틸리티
 *
 * @author y_ha
 */
@Component
public class DateUtils {

    @Value("${app.datetime.format}")
    private String systemDateFormat;

    private static final String DATE_FORMAT_YMDHMS_HYPHEN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_YMDHM_HYPHEN = "yyyy-MM-dd HH:mm";
    private static final String DATE_FORMAT_YMDHM = "yyyy/MM/dd HH:mm";
    private static final String DATE_FORMAT_YMDHMS = "yyyy/MM/dd HH:mm:ss";
    private static final String DATE_FORMAT_YMDHMS_S = "yyyy/MM/dd HH:mm:ss.SSS";

    private static final String DATE_FORMAT_FOR_S3 = "yyHHddHHmmss";

    public int compare(String datetimeA, String datetimeB, int diff, int unit) {
        return compare(datetimeA, datetimeB, diff, unit, systemDateFormat);
    }

    public int compare(String datetimeA, String datetimeB, int diff, int unit, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime a = LocalDateTime.parse(datetimeA, formatter);
        LocalDateTime b = LocalDateTime.parse(datetimeB, formatter);

        return compare(a, b, diff, unit);
    }

    public int compare(LocalDateTime datetimeA, LocalDateTime datetimeB, int diff, int unit) {
        LocalDateTime calA = datetimeA;
        LocalDateTime calB = datetimeB.plus(diff, getChronoUnit(unit));
        return calA.compareTo(calB);
    }

    public static boolean isFutureTime(Date targetTime) {
        if (targetTime == null) {
            return false;
        }
        return targetTime.after(new Date());
    }

    public boolean inRange(LocalDateTime target, LocalDateTime rangeFrom, LocalDateTime rangeTo) {
        return target.compareTo(rangeFrom) >= 0 && target.compareTo(rangeTo) <= 0;
    }

    public static LocalDateTime getFormatDateYmdHm(String dateStr) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT_YMDHM);
        try {
            return LocalDateTime.parse(dateStr, dateFormat);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    public LocalDateTime convertSystemDateFormat(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(systemDateFormat);
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    public String getFormatDateYmdHm(LocalDateTime dateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT_YMDHM);
        return dateTime.format(dateFormat);
    }

    public static int getDateDuration(LocalDateTime fromDate, LocalDateTime toDate) {
        Duration duration = Duration.between(fromDate, toDate);
        return (int) duration.toDays();
    }

    public static String formatYmdHmHyphen(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        return sdf.format(date);
    }

    public static LocalDateTime getCurrentTime() {
        String currentTime = getCurrentTimeAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YMDHMS_HYPHEN);
        formatter = formatter.withZone(ZoneId.of("Asia/Tokyo"));
        try {
            return LocalDateTime.parse(currentTime, formatter);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    public static Date getDateNow() {
        return Date.from(DateUtils.getCurrentTime().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getCurrentTimeAsString() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YMDHMS_HYPHEN);
        formatter.withZone(ZoneId.of("Asia/Tokyo"));
        return formatter.format(LocalDateTime.now());
    }

    public static String getCurrentTimeForS3() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_FOR_S3);
        formatter.withZone(ZoneId.of("Asia/Tokyo"));
        return "_" + formatter.format(LocalDateTime.now());
    }

    private ChronoUnit getChronoUnit(int unit) {
        switch (unit) {
            case Calendar.DATE:
                return java.time.temporal.ChronoUnit.DAYS;
            case Calendar.MONTH:
                return java.time.temporal.ChronoUnit.MONTHS;
            case Calendar.YEAR:
                return java.time.temporal.ChronoUnit.YEARS;
            default:
                throw new IllegalArgumentException("Invalid unit value: " + unit);
        }
    }

    public static boolean isToday(Date targetDate) {
        if (targetDate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String targetStr = sdf.format(targetDate);
        String todayStr = sdf.format(new Date());

        return targetStr.equals(todayStr);
    }

    public static Date parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr + ", pattern: " + pattern);
        }
    }
}