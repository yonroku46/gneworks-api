package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;

public class UserAssignedRegionDetailRes extends ResponseData {
    private String assignedRegionId;
    private String userId;
    private String regionId;
    private String sido;
    private String sigungu;
    private String assignedDate;

    public String getAssignedRegionId() { return assignedRegionId; }
    public void setAssignedRegionId(String assignedRegionId) { this.assignedRegionId = assignedRegionId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getRegionId() { return regionId; }
    public void setRegionId(String regionId) { this.regionId = regionId; }
    public String getSido() { return sido; }
    public void setSido(String sido) { this.sido = sido; }
    public String getSigungu() { return sigungu; }
    public void setSigungu(String sigungu) { this.sigungu = sigungu; }
    public String getAssignedDate() { return assignedDate; }
    public void setAssignedDate(String assignedDate) { this.assignedDate = assignedDate; }
}
