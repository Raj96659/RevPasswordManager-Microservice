package com.revpasswordmanager.notification_service.dto;

import lombok.Data;

@Data
public class NotificationRequest {

    private Long userId;
    private String message;
    private String type;

}