package com.revpasswordmanager.security_service.service;

import com.revpasswordmanager.security_service.client.NotificationClient;
import com.revpasswordmanager.security_service.dto.NotificationRequest;
import com.revpasswordmanager.security_service.entity.SecurityAudit;
import com.revpasswordmanager.security_service.repository.SecurityAuditRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        boolean breached = passwordService.isBreachedPassword(password);

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

        if(breached){

            NotificationRequest req = new NotificationRequest();

            req.setUserId(userId);
            req.setMessage("⚠ This password appears in known data breaches");
            req.setType("SECURITY_ALERT");

            notificationClient.sendNotification(req);
        }

        return repository.save(audit);
    }


    private String hashPassword(String password){
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    public Map<String,Integer> getSecurityAudit(Long userId){

        List<SecurityAudit> audits = repository.findByUserId(userId);

        int weak = 0;
        int reused = 0;
        int strong = 0;

        for(SecurityAudit audit : audits){

            if(audit.isWeakPassword()){
                weak++;
            }
            else if(audit.getStrengthScore() >= 70){
                strong++;
            }

            if(audit.isReusedPassword()){
                reused++;
            }
        }

        Map<String,Integer> result = new HashMap<>();

        result.put("weakPasswords", weak);
        result.put("strongPasswords", strong);
        result.put("reusedPasswords", reused);

        return result;
    }

}