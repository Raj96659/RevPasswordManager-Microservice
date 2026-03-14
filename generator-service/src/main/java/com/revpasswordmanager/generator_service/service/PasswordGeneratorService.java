package com.revpasswordmanager.generator_service.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordGeneratorService {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()";

    public String generatePassword(int length,
                                   boolean upper,
                                   boolean numbers,
                                   boolean symbols) {

        String characters = LOWER;

        if (upper) characters += UPPER;
        if (numbers) characters += NUMBERS;
        if (symbols) characters += SYMBOLS;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }
}