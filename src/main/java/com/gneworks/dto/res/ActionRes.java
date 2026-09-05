package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;

public class ActionRes extends ResponseData {
    private boolean success;
    private String id;

    public ActionRes() {}

    public ActionRes(String id) {
        this.success = true;
        this.id = id;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
