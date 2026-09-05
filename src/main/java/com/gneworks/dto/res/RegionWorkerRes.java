package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import java.util.List;

public class RegionWorkerRes extends ResponseData {
    private String userId;
    private String userName;
    private String phoneNum;
    private String profileImg;
    private String lastUpdated;
    private String createTime;
    private List<UserAssignedRegionDetailRes> assignedRegions;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPhoneNum() { return phoneNum; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }
    public String getProfileImg() { return profileImg; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }
    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public List<UserAssignedRegionDetailRes> getAssignedRegions() { return assignedRegions; }
    public void setAssignedRegions(List<UserAssignedRegionDetailRes> assignedRegions) { this.assignedRegions = assignedRegions; }
}
