package com.gneworks.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminDeleteReportReq {
    @NotBlank(message = "삭제 사유를 입력해 주세요.")
    private String deleteReason;
}