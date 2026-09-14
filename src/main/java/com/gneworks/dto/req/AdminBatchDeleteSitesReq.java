package com.gneworks.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AdminBatchDeleteSitesReq {
    @NotEmpty(message = "삭제할 현장 ID 목록을 지정해 주세요.")
    private List<String> siteIds;
}
