package com.gneworks.dto.req;

import lombok.Data;

@Data
public class AdminInquirySearchReq {
    private String status;         // "ALL", "RESOLVED", "WAITING"
    private String inquiryType;    // "ALL", or specific type code
    private String startDate;      // YYYY-MM-DD
    private String endDate;        // YYYY-MM-DD
    private String query;          // user_name, user_id, phone_num, inquiry_contents
    private Integer page;
    private Integer size;

    public int getPage() {
        return (page != null && page > 0) ? page : 1;
    }

    public int getSize() {
        return (size != null && size > 0) ? size : 30;
    }

    public int getOffset() {
        return (getPage() - 1) * getSize();
    }
}
