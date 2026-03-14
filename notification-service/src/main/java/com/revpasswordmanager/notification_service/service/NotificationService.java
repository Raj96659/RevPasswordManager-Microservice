package com.revpasswordmanager.notification_service.service;

import com.revpasswordmanager.notification_service.entity.Notification;
import com.revpasswordmanager.notification_service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public Notification createNotification(Long userId,
                                           String message,
                                           String type){

        Notification notification = new Notification();

        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReadStatus(false);
        notification.setCreatedAt(LocalDateTime.now());

        return repository.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId){
        return repository.findByUserId(userId);
    }
}