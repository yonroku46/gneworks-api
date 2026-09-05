package com.gneworks.dto.req;

public class AdminHouseholdReq {
    private String dong;
    private String ho;
    private String headName;
    private String targetType;
    private String remarks;
    private String installStatus;

    public String getDong() { return dong; }
    public void setDong(String dong) { this.dong = dong; }
    public String getHo() { return ho; }
    public void setHo(String ho) { this.ho = ho; }
    public String getHeadName() { return headName; }
    public void setHeadName(String headName) { this.headName = headName; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getInstallStatus() { return installStatus; }
    public void setInstallStatus(String installStatus) { this.installStatus = installStatus; }
}
