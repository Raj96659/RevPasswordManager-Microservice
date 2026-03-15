package com.revpasswordmanager.vault_service.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String masterPassword;
    private String email;

}