package com.revpasswordmanager.vault_service.dto;

import lombok.Data;

@Data
public class SecurityRequestDTO {

    private Long userId;
    private String website;
    private String password;

}
