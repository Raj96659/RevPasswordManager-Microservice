package com.revpasswordmanager.security_service.client;

import com.revpasswordmanager.security_service.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

    @PostMapping("/notifications/create")
    void sendNotification(@RequestBody NotificationRequest request);

}