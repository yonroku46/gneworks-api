package com.gneworks.dto.res;

import lombok.Data;

@Data
public class AdminWorkerStatRes {
    private String userId;
    private String name;
    private String phone;
    private String profileImg;
    private int total;
    private int completed;
    private int pending;
    private int rejected;
}
