package com.revpasswordmanager.security_service.service;

import com.revpasswordmanager.security_service.entity.SecurityAudit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordSecurityService {

    public int calculateStrength(String password) {

        int score = 0;

        if(password.length() >= 8) score++;
        if(password.length() >= 12) score++;

        if(password.matches(".*[A-Z].*")) score++;
        if(password.matches(".*[0-9].*")) score++;
        if(password.matches(".*[!@#$%^&*()].*")) score++;

        return score;
    }

    public boolean isWeak(String password){
        return calculateStrength(password) < 3;
    }

    public boolean isReusedPassword(String password, List<SecurityAudit> audits){

        for(SecurityAudit audit : audits){

            if(audit.getWebsite() != null &&
                    password.equals(audit.getWebsite())){

                return true;
            }
        }

        return false;
    }

    public boolean isBreachedPassword(String password){

        List<String> breachedPasswords = List.of(
                "123456",
                "password",
                "123456789",
                "qwerty",
                "abc123",
                "letmein",
                "welcome",
                "admin",
                "password123"
        );

        return breachedPasswords.contains(password.toLowerCase());
    }
}