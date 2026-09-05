package com.gneworks.dto.res;

import com.gneworks.dao.entity.Household;
import com.gneworks.dto.res.core.ResponseData;
import java.util.Date;
import java.util.List;

public class AdminSiteRes extends ResponseData {
    private String siteId;
    private String regionId;
    private String name;
    private String region;
    private String address;
    private String sido;
    private String sigungu;
    private String eupmyeondong;
    private String contactPhone;
    private Date createTime;

    private int dongCount;
    private int totalHouseholds;
    private int completedHouseholds;

    private List<Household> households;
    private List<RegionWorkerRes> assignedWorkers;

    public String getSiteId() { return siteId; }
    public void setSiteId(String siteId) { this.siteId = siteId; }
    public String getRegionId() { return regionId; }
    public void setRegionId(String regionId) { this.regionId = regionId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getSido() { return sido; }
    public void setSido(String sido) { this.sido = sido; }
    public String getSigungu() { return sigungu; }
    public void setSigungu(String sigungu) { this.sigungu = sigungu; }
    public String getEupmyeondong() { return eupmyeondong; }
    public void setEupmyeondong(String eupmyeondong) { this.eupmyeondong = eupmyeondong; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public int getDongCount() { return dongCount; }
    public void setDongCount(int dongCount) { this.dongCount = dongCount; }
    public int getTotalHouseholds() { return totalHouseholds; }
    public void setTotalHouseholds(int totalHouseholds) { this.totalHouseholds = totalHouseholds; }
    public int getCompletedHouseholds() { return completedHouseholds; }
    public void setCompletedHouseholds(int completedHouseholds) { this.completedHouseholds = completedHouseholds; }
    public List<Household> getHouseholds() { return households; }
    public void setHouseholds(List<Household> households) { this.households = households; }
    public List<RegionWorkerRes> getAssignedWorkers() { return assignedWorkers; }
    public void setAssignedWorkers(List<RegionWorkerRes> assignedWorkers) { this.assignedWorkers = assignedWorkers; }
}
