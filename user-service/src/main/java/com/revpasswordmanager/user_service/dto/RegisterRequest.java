package com.revpasswordmanager.user_service.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String email;

    private String username;

    private String masterPassword;

    private boolean twoFactorEnabled;

    private String securityQuestion1;
    private String securityAnswer1;

    private String securityQuestion2;
    private String securityAnswer2;

    private String securityQuestion3;
    private String securityAnswer3;

}