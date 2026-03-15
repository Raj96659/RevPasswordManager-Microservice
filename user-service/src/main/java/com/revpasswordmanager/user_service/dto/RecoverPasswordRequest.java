package com.revpasswordmanager.user_service.dto;
import lombok.Data;

@Data
public class RecoverPasswordRequest {

    private String email;
    private String answer1;
    private String answer2;
    private String answer3;
    private String newPassword;

}
