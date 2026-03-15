package com.revpasswordmanager.vault_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revpasswordmanager.vault_service.client.SecurityServiceClient;
import com.revpasswordmanager.vault_service.client.UserServiceClient;
import com.revpasswordmanager.vault_service.dto.SecurityRequestDTO;
import com.revpasswordmanager.vault_service.dto.UserDTO;
import com.revpasswordmanager.vault_service.dto.VaultRequest;
import com.revpasswordmanager.vault_service.entity.Vault;
import com.revpasswordmanager.vault_service.repository.VaultRepository;
import com.revpasswordmanager.vault_service.util.EncryptionUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class VaultService {

    @Autowired
    private VaultRepository vaultRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private SecurityServiceClient securityClient;


    /**
     * Save credential with security analysis + encryption
     */
    @CircuitBreaker(name = "securityService", fallbackMethod = "securityFallback")
    public Vault saveCredential(VaultRequest request) throws Exception {

        // 1️⃣ Check if user exists
        UserDTO user = userServiceClient.getUserById(request.getUserId());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 2️⃣ Send password to security service for analysis
        SecurityRequestDTO dto = new SecurityRequestDTO();
        dto.setUserId(request.getUserId());
        dto.setWebsite(request.getWebsite());
        dto.setPassword(request.getPassword());

        System.out.println("Calling security service...");
        securityClient.analyzePassword(dto);

        // 3️⃣ Encrypt password before saving
        byte[] salt = request.getUserId().toString().getBytes();

        SecretKey key =
                EncryptionUtil.deriveKey(user.getMasterPassword(), salt);


        byte[] iv = EncryptionUtil.generateIV();

        String encryptedPassword =
                EncryptionUtil.encrypt(request.getPassword(), key, iv);

        // 4️⃣ Create vault entity
        Vault vault = new Vault();

        vault.setUserId(request.getUserId());
        vault.setWebsite(request.getWebsite());
        vault.setUsername(request.getUsername());
        vault.setEncryptedPassword(encryptedPassword);
        vault.setIv(Base64.getEncoder().encodeToString(iv));
        vault.setCategory(request.getCategory());
        vault.setFavorite(request.isFavorite());
        vault.setNotes(request.getNotes());

        // 5️⃣ Save to database
        return vaultRepository.save(vault);
    }


    /**
     * Circuit Breaker fallback
     * Executes if Security Service is DOWN
     */
    public Vault securityFallback(VaultRequest request, Throwable ex) throws Exception {

        System.out.println("⚠ Security service unavailable. Saving credential without analysis.");

        // Encrypt password even in fallback
        if(request.getUserId() == null){
            throw new RuntimeException("UserId is missing in request");
        }

        byte[] salt = request.getUserId().toString().getBytes();

        SecretKey key =
                EncryptionUtil.deriveKey(request.getMasterPassword(), salt);

        byte[] iv = EncryptionUtil.generateIV();

        String encryptedPassword =
                EncryptionUtil.encrypt(request.getPassword(), key, iv);

        Vault vault = new Vault();

        vault.setUserId(request.getUserId());
        vault.setWebsite(request.getWebsite());
        vault.setUsername(request.getUsername());
        vault.setEncryptedPassword(encryptedPassword);
        vault.setIv(Base64.getEncoder().encodeToString(iv));
        vault.setCategory(request.getCategory());
        vault.setFavorite(request.isFavorite());
        vault.setNotes(request.getNotes());

        return vaultRepository.save(vault);
    }


    /**
     * Fetch all vault entries of a user
     */
    public List<Vault> getUserVault(Long userId) {

        return vaultRepository.findByUserId(userId);
    }

    public List<Vault> searchVault(Long userId, String query){

        List<Vault> byWebsite =
                vaultRepository.findByUserIdAndWebsiteContainingIgnoreCase(userId, query);

        List<Vault> byUsername =
                vaultRepository.findByUserIdAndUsernameContainingIgnoreCase(userId, query);

        Set<Vault> result = new HashSet<>();

        result.addAll(byWebsite);
        result.addAll(byUsername);

        return new ArrayList<>(result);
    }

    public List<Vault> filterByCategory(Long userId, String category){

        return vaultRepository.findByUserIdAndCategory(userId, category);
    }

    public List<Vault> getFavoritesFirst(Long userId){
        return vaultRepository.findByUserIdAndFavoriteTrue(userId);
    }


    public String exportVault(Long userId) throws Exception {

        List<Vault> vaultEntries = vaultRepository.findByUserId(userId);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(vaultEntries);
    }

    public void importVault(Long userId, MultipartFile file) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        List<Vault> entries = Arrays.asList(
                mapper.readValue(file.getInputStream(), Vault[].class)
        );

        for(Vault vault : entries){

            vault.setUserId(userId);

            vaultRepository.save(vault);
        }
    }

    public void deleteVault(Long id){
        vaultRepository.deleteById(id);
    }

    public String decryptPassword(Long id, String masterPassword) throws Exception {

        Vault vault = vaultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vault not found"));

        byte[] salt = vault.getUserId().toString().getBytes();

        SecretKey key = EncryptionUtil.deriveKey(masterPassword, salt);

        byte[] iv = Base64.getDecoder().decode(vault.getIv());

        return EncryptionUtil.decrypt(vault.getEncryptedPassword(), key, iv);
    }




}