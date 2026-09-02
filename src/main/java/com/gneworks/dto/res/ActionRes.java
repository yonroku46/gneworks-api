package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActionRes extends ResponseData {
    private boolean success;
    private String id;
}
