package com.revpasswordmanager.vault_service.client;

import com.revpasswordmanager.vault_service.dto.SecurityAuditDTO;
import com.revpasswordmanager.vault_service.dto.SecurityRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "SECURITY-SERVICE")
public interface SecurityServiceClient {

    @PostMapping("/security/analyze")
    SecurityAuditDTO analyzePassword(SecurityRequestDTO request);

}