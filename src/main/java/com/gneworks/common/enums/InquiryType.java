package com.gneworks.common.enums;

import lombok.Getter;

@Getter
public enum InquiryType {
    PASSWORD_RESET("password_reset", "비밀번호 재발급"),
    ACCOUNT("account", "계정/권한 문의"),
    TASK_REPORT("task_report", "작업배정/현장보고"),
    BUG("bug", "오류/버그 신고"),
    FEATURE("feature", "기능개선 제안"),
    GENERAL("general", "일반/기타 문의");

    private final String code;
    private final String label;

    InquiryType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static String toLabel(String code) {
        if (code == null || code.isBlank()) {
            return "업무 문의";
        }
        for (InquiryType type : values()) {
            if (type.code.equalsIgnoreCase(code.trim())) {
                return type.label;
            }
        }
        return code;
    }
}
