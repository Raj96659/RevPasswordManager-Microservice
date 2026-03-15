package com.revpasswordmanager.security_service.controller;

import com.revpasswordmanager.security_service.entity.SecurityAudit;
import com.revpasswordmanager.security_service.service.SecurityAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private SecurityAuditService auditService;

    @PostMapping("/analyze")
    public SecurityAudit analyze(@RequestBody Map<String,String> request){

        Long userId = Long.parseLong(request.get("userId"));
        String website = request.get("website");
        String password = request.get("password");

        return auditService.analyzePassword(userId, website, password);
    }

    @GetMapping("/audit/{userId}")
    public Map<String,Integer> getAudit(@PathVariable Long userId){

        return auditService.getSecurityAudit(userId);
    }
}