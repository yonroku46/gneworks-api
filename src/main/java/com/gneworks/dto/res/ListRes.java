package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ListRes<T> extends ResponseData {
    private List<T> list;
    private int totalCount;

    public ListRes(List<T> list) {
        this.list = list;
    }
}
