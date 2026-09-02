package com.gneworks.dto.res.core;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 응답 클래스
 *
 * @author y_ha
 */
@Data
@NoArgsConstructor
public class BaseResponse<T> {

    private int resultCode;

    private boolean hasErrors;

    private List<Information> informations;

    private List<Error> errors;

    private ResponseData responseData;
}
