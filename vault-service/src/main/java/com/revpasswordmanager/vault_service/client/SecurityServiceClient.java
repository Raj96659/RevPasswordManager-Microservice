package com.revpasswordmanager.vault_service.client;

import com.revpasswordmanager.vault_service.config.FeignConfig;
import com.revpasswordmanager.vault_service.dto.SecurityAuditDTO;
import com.revpasswordmanager.vault_service.dto.SecurityRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "SECURITY-SERVICE")
@FeignClient(name = "SECURITY-SERVICE", configuration = FeignConfig.class)
public interface SecurityServiceClient {

    @PostMapping("/security/analyze")
    SecurityAuditDTO analyzePassword(@RequestBody SecurityRequestDTO request);

}