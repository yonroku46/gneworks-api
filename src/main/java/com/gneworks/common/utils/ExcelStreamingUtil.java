package com.gneworks.common.utils;

import com.gneworks.common.annotation.ExcelColumn;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DTO의 @ExcelColumn 어노테이션을 기반으로 대용량(수만~수십만 건) 데이터를
 * OutOfMemory 없이 고속으로 엑셀 스트리밍 다운로드하는 범용 유틸리티.
 */
@Slf4j
public class ExcelStreamingUtil {

    private static class ColumnInfo {
        Field field;
        Method getter;
        ExcelColumn annotation;

        ColumnInfo(Field field, ExcelColumn annotation, Method getter) {
            this.field = field;
            this.annotation = annotation;
            this.getter = getter;
            this.field.setAccessible(true);
            if (this.getter != null) {
                this.getter.setAccessible(true);
            }
        }
    }

    /**
     * DTO 리스트를 대용량 엑셀 파일로 스트리밍 출력
     *
     * @param response     HttpServletResponse
     * @param fileBaseName 다운로드 파일 기본명 (예: "현장목록", "계정목록")
     * @param dataList     출력할 데이터 목록
     * @param clazz        데이터 DTO 클래스 타입
     */
    public static <T> void export(
            HttpServletResponse response,
            String fileBaseName,
            List<T> dataList,
            Class<T> clazz
    ) throws IOException {

        // 1. @ExcelColumn 어노테이션이 부여된 필드 수집 및 정렬
        List<ColumnInfo> columns = extractColumns(clazz);

        // 2. SXSSFWorkbook 생성 (메모리 100행 유지 후 디스크 임시 파일 플러시)
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet(fileBaseName != null ? fileBaseName : "Sheet1");

            // 3. 스타일 세팅
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setBorderTop(BorderStyle.THIN);
            bodyStyle.setBorderBottom(BorderStyle.THIN);
            bodyStyle.setBorderLeft(BorderStyle.THIN);
            bodyStyle.setBorderRight(BorderStyle.THIN);
            bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.cloneStyleFrom(bodyStyle);
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle rightStyle = workbook.createCellStyle();
            rightStyle.cloneStyleFrom(bodyStyle);
            rightStyle.setAlignment(HorizontalAlignment.RIGHT);

            // 4. 헤더 행 작성 (0번 컬럼: 순번)
            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(24);

            Cell noHeaderCell = headerRow.createCell(0);
            noHeaderCell.setCellValue("순번");
            noHeaderCell.setCellStyle(headerStyle);
            sheet.setColumnWidth(0, 8 * 256);

            for (int i = 0; i < columns.size(); i++) {
                ColumnInfo col = columns.get(i);
                Cell cell = headerRow.createCell(i + 1);
                cell.setCellValue(col.annotation.header());
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i + 1, Math.max(10, col.annotation.width()) * 256);
            }

            // 날짜 포맷 캐시
            Map<String, SimpleDateFormat> dateFormatMap = new HashMap<>();

            // 5. 데이터 행 작성
            int rowNum = 1;
            if (dataList != null) {
                for (T item : dataList) {
                    Row row = sheet.createRow(rowNum);

                    // 순번 셀
                    Cell noCell = row.createCell(0);
                    noCell.setCellValue(rowNum);
                    noCell.setCellStyle(centerStyle);

                    // 각 컬럼 필드값 주입
                    for (int colIdx = 0; colIdx < columns.size(); colIdx++) {
                        ColumnInfo col = columns.get(colIdx);
                        Cell cell = row.createCell(colIdx + 1);

                        try {
                            Object val = null;
                            if (col.getter != null) {
                                try {
                                    val = col.getter.invoke(item);
                                } catch (Exception ignored) {
                                    val = col.field.get(item);
                                }
                            } else {
                                val = col.field.get(item);
                            }
                            setCellValue(cell, val, col.annotation, dateFormatMap, bodyStyle, centerStyle, rightStyle);
                        } catch (IllegalAccessException e) {
                            cell.setCellValue("");
                            cell.setCellStyle(bodyStyle);
                        }
                    }

                    rowNum++;
                }
            }

            // 6. 응답 헤더 및 파일명 설정 (UTF-8 인코딩)
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String rawFileName = (fileBaseName != null ? fileBaseName : "export") + "_" + timestamp + ".xlsx";
            String encodedFileName = URLEncoder.encode(rawFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

            // 7. 스트리밍 출력 및 임시 파일 삭제
            workbook.write(response.getOutputStream());
            response.flushBuffer();
            workbook.dispose();
        }
    }

    private static <T> List<ColumnInfo> extractColumns(Class<T> clazz) {
        List<ColumnInfo> list = new ArrayList<>();
        Class<?> current = clazz;

        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                if (annotation != null) {
                    Method getter = findGetter(current, field);
                    list.add(new ColumnInfo(field, annotation, getter));
                }
            }
            current = current.getSuperclass();
        }

        // order 기준으로 정렬
        list.sort(Comparator.comparingInt(a -> a.annotation.order()));
        return list;
    }

    private static Method findGetter(Class<?> clazz, Field field) {
        String name = field.getName();
        String capitalized = Character.toUpperCase(name.charAt(0)) + (name.length() > 1 ? name.substring(1) : "");
        String[] possibleNames = new String[]{
                "get" + capitalized,
                "is" + capitalized,
                name
        };
        for (String getterName : possibleNames) {
            try {
                return clazz.getMethod(getterName);
            } catch (NoSuchMethodException ignored) {
            }
        }
        return null;
    }

    private static void setCellValue(
            Cell cell,
            Object val,
            ExcelColumn annotation,
            Map<String, SimpleDateFormat> dateFormatMap,
            CellStyle bodyStyle,
            CellStyle centerStyle,
            CellStyle rightStyle
    ) {
        if (val == null) {
            cell.setCellValue("");
            cell.setCellStyle(bodyStyle);
            return;
        }

        String align = annotation.align();

        if (val instanceof Number) {
            cell.setCellValue(((Number) val).doubleValue());
            if ("CENTER".equalsIgnoreCase(align)) {
                cell.setCellStyle(centerStyle);
            } else if ("LEFT".equalsIgnoreCase(align)) {
                cell.setCellStyle(bodyStyle);
            } else {
                cell.setCellStyle(rightStyle); // 기본 우측 정렬
            }
        } else if (val instanceof Date) {
            String pattern = annotation.dateFormat();
            SimpleDateFormat sdf = dateFormatMap.computeIfAbsent(pattern, SimpleDateFormat::new);
            cell.setCellValue(sdf.format((Date) val));
            cell.setCellStyle("LEFT".equalsIgnoreCase(align) ? bodyStyle : centerStyle);
        } else if (val instanceof Boolean) {
            cell.setCellValue((Boolean) val ? "Y" : "N");
            cell.setCellStyle(centerStyle);
        } else {
            cell.setCellValue(val.toString());
            if ("CENTER".equalsIgnoreCase(align)) {
                cell.setCellStyle(centerStyle);
            } else if ("RIGHT".equalsIgnoreCase(align)) {
                cell.setCellStyle(rightStyle);
            } else {
                cell.setCellStyle(bodyStyle);
            }
        }
    }
}
