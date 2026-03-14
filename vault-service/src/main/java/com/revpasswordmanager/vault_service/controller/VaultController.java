package com.revpasswordmanager.vault_service.controller;

import com.revpasswordmanager.vault_service.dto.VaultRequest;
import com.revpasswordmanager.vault_service.entity.Vault;
import com.revpasswordmanager.vault_service.service.VaultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}