package com.gneworks.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AdminBatchDeleteReportsReq {
    @NotEmpty(message = "삭제할 보고서 ID 목록을 지정해 주세요.")
    private List<String> reportIds;

    @NotBlank(message = "삭제 사유를 입력해 주세요.")
    private String deleteReason;
}
