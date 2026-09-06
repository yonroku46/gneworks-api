package com.gneworks.dto.res;

import com.gneworks.dao.entity.Household;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HouseholdRes extends Household {
    private String reportId;
    private String reportStatus;
    private String reportUserId;
    private String reporterName;
    private String reportTime;
}