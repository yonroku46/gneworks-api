package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorkReportRes extends ResponseData {
    private String reportId;
    private String householdId;
    private String siteId;
    private String userId;
    private String dong;
    private String ho;
    private String headName;
    private String installDate;
    private String reportTime;
    private String reporterName;
    private String confirmerName;
    private String confirmerSignature;
    private String photoDoor;
    private String photoBefore1;
    private String photoAfter1;
    private String photoBefore2;
    private String photoAfter2;
    private String status;
    private String fixReason;
    private String remarks;
    private Date createTime;
    private Date lastUpdate;
}
