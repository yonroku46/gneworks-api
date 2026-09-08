package com.gneworks.dto.req;

import lombok.Data;

@Data
public class PushSubscriptionReq {
    private String endpoint;
    private String p256dh;
    private String auth;
    private String userAgent;
}