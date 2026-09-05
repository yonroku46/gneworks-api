package com.gneworks.dto.req;

public class AdminSiteReq {
    private String name;
    private String address;
    private String regionId;
    private String region;
    private String sido;
    private String sigungu;
    private String eupmyeondong;
    private String contactPhone;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRegionId() { return regionId; }
    public void setRegionId(String regionId) { this.regionId = regionId; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getSido() { return sido; }
    public void setSido(String sido) { this.sido = sido; }
    public String getSigungu() { return sigungu; }
    public void setSigungu(String sigungu) { this.sigungu = sigungu; }
    public String getEupmyeondong() { return eupmyeondong; }
    public void setEupmyeondong(String eupmyeondong) { this.eupmyeondong = eupmyeondong; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
}
