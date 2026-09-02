package com.gneworks.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class RefreshReq extends ResponseData {
    private String refreshToken;
}