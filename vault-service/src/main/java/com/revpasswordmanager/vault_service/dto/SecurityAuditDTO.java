package com.revpasswordmanager.vault_service.dto;

import lombok.Data;

@Data
public class SecurityAuditDTO {

    private Long id;
    private Long userId;
    private String website;
    private String passwordStrength;
    private String status;

}