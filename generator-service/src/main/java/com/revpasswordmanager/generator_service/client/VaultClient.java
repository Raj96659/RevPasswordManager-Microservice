package com.revpasswordmanager.generator_service.client;

import com.revpasswordmanager.generator_service.dto.VaultRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "VAULT-SERVICE")
public interface VaultClient {

    @PostMapping("/vault/add")
    VaultRequest saveCredential(@RequestBody VaultRequest request);

}
