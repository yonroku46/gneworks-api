package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
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
    private List<HouseholdRes> households;
    private List<RegionWorkerRes> assignedWorkers;
}
