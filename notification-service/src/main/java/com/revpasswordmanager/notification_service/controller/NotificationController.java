package com.revpasswordmanager.notification_service.controller;

import com.revpasswordmanager.notification_service.dto.NotificationRequest;
import com.revpasswordmanager.notification_service.entity.Notification;
import com.revpasswordmanager.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/create")
    public Notification create(@RequestBody NotificationRequest request){

        return notificationService.createNotification(
                request.getUserId(),
                request.getMessage(),
                request.getType()
        );
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Long userId){

        return notificationService.getUserNotifications(userId);
    }
}