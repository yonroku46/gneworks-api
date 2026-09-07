package com.gneworks.dto.res;

import com.gneworks.dto.res.core.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminImportResultRes extends ResponseData {
    private int siteInserted;
    private int siteSkipped;
    private int householdInserted;
    private int householdSkipped;
    private String regionName;
}
