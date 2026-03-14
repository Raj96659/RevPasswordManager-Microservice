package com.revpasswordmanager.vault_service.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil {

    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 65536;
    private static final int IV_LENGTH = 12;

    public static SecretKey deriveKey(String password, byte[] salt) throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                ITERATIONS,
                KEY_SIZE
        );

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] keyBytes = factory.generateSecret(spec).getEncoded();

        return new SecretKeySpec(keyBytes, "AES");
    }

    public static byte[] generateIV() {

        byte[] iv = new byte[IV_LENGTH];

        new SecureRandom().nextBytes(iv);

        return iv;
    }

    public static String encrypt(String data, SecretKey key, byte[] iv) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] encrypted = cipher.doFinal(data.getBytes());

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedData, SecretKey key, byte[] iv) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] decoded = Base64.getDecoder().decode(encryptedData);

        byte[] decrypted = cipher.doFinal(decoded);

        return new String(decrypted);
    }
}