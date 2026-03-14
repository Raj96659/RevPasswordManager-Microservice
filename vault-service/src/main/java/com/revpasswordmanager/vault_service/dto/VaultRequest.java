package com.revpasswordmanager.vault_service.dto;

import lombok.Data;

@Data
public class VaultRequest {

    private Long userId;

    private String website;

    private String username;

    private String password;

    private String masterPassword;

    private String category;

    private boolean favorite;

    private String notes;
}