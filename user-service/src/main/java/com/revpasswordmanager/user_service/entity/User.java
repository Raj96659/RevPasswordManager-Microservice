package com.revpasswordmanager.user_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String masterPassword;

    private boolean twoFactorEnabled;

    private String otp;

    private LocalDateTime otpExpiry;

    private String securityQuestion1;
    private String securityAnswer1;

    private String securityQuestion2;
    private String securityAnswer2;

    private String securityQuestion3;
    private String securityAnswer3;

}