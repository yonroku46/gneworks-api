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
public class PageRes<T> extends ResponseData {
    private List<T> list;
    private long totalCount;
    private int page;
    private int size;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrev;

    public PageRes(List<T> list, long totalCount, int page, int size) {
        this.list = list;
        this.totalCount = totalCount;
        this.page = page;
        this.size = size;
        this.totalPages = size > 0 ? (int) Math.ceil((double) totalCount / size) : 0;
        this.hasNext = page < this.totalPages;
        this.hasPrev = page > 1;
    }
}