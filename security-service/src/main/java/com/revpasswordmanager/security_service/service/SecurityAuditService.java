package com.revpasswordmanager.security_service.service;

import com.revpasswordmanager.security_service.client.NotificationClient;
import com.revpasswordmanager.security_service.dto.NotificationRequest;
import com.revpasswordmanager.security_service.entity.SecurityAudit;
import com.revpasswordmanager.security_service.repository.SecurityAuditRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class SecurityAuditService {

    @Autowired
    private SecurityAuditRepository repository;

    @Autowired
    private PasswordSecurityService passwordService;

    @Autowired
    private NotificationClient notificationClient;


    public SecurityAudit analyzePassword(Long userId,
                                         String website,
                                         String password){

        // 1️⃣ Calculate strength
        int score = passwordService.calculateStrength(password);

        boolean weak = passwordService.isWeak(password);

        // 2️⃣ Hash password
        String hash = hashPassword(password);

        // 3️⃣ Fetch previous passwords
        List<SecurityAudit> audits = repository.findByUserId(userId);

        boolean reused = audits.stream()
                .anyMatch(a -> hash.equals(a.getPasswordHash()));

        // 4️⃣ Create audit record
        SecurityAudit audit = new SecurityAudit();

        audit.setUserId(userId);
        audit.setWebsite(website);
        audit.setStrengthScore(score);
        audit.setWeakPassword(weak);
        audit.setReusedPassword(reused);
        audit.setPasswordHash(hash);

        // 5️⃣ Send notification if weak
        if(weak){

            NotificationRequest req = new NotificationRequest();

            req.setUserId(userId);
            req.setMessage("Weak password detected for " + website);
            req.setType("SECURITY_ALERT");

            notificationClient.sendNotification(req);
        }

        // 🚨 Notification if password reused
        if(reused){

            NotificationRequest req = new NotificationRequest();

            req.setUserId(userId);
            req.setMessage("Password reuse detected for " + website);
            req.setType("SECURITY_ALERT");

            notificationClient.sendNotification(req);
        }

        return repository.save(audit);
    }


    private String hashPassword(String password){
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

}