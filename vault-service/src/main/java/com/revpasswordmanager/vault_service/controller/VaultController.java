package com.revpasswordmanager.vault_service.controller;

import com.revpasswordmanager.vault_service.dto.VaultRequest;
import com.revpasswordmanager.vault_service.entity.Vault;
import com.revpasswordmanager.vault_service.service.VaultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vault")
public class VaultController {

    @Autowired
    private VaultService vaultService;

    @PostMapping("/add")
    public Vault addCredential(@RequestBody VaultRequest request) throws Exception {
        return vaultService.saveCredential(request);
    }

    @GetMapping("/user/{userId}")
    public List<Vault> getUserVault(@PathVariable Long userId) {
        return vaultService.getUserVault(userId);
    }

    @GetMapping("/search")
    public List<Vault> search(@RequestParam Long userId,
                              @RequestParam String query){

        return vaultService.searchVault(userId, query);
    }

    @GetMapping("/filter")
    public List<Vault> filter(@RequestParam Long userId,
                              @RequestParam String category){

        return vaultService.filterByCategory(userId, category);
    }

    @GetMapping("/sort/favorites")
    public List<Vault> sortFavorites(@RequestParam Long userId){

        return vaultService.getFavoritesFirst(userId);
    }

    @GetMapping("/export/{userId}")
    public ResponseEntity<String> exportVault(@PathVariable Long userId) throws Exception {

        String backup = vaultService.exportVault(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=vault-backup.json")
                .body(backup);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importVault(@RequestParam Long userId,
                                              @RequestParam MultipartFile file)
            throws Exception {

        vaultService.importVault(userId, file);

        return ResponseEntity.ok("Vault imported successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVault(@PathVariable Long id){

        vaultService.deleteVault(id);

        return ResponseEntity.ok("Credential deleted");

    }

    @PostMapping("/decrypt")
    public String decryptPassword(@RequestBody Map<String,String> req) throws Exception {

        Long id = Long.parseLong(req.get("id"));
        String masterPassword = req.get("masterPassword");

        return vaultService.decryptPassword(id, masterPassword);
    }





}