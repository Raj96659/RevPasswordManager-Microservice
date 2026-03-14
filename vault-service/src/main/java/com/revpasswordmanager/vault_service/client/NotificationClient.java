package com.revpasswordmanager.vault_service.client;

import com.revpasswordmanager.vault_service.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

    @PostMapping("/notifications/create")
    void sendNotification(NotificationRequest request);

}