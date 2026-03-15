package com.revpasswordmanager.vault_service.repository;

import com.revpasswordmanager.vault_service.entity.Vault;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaultRepository extends JpaRepository<Vault, Long> {

    List<Vault> findByUserId(Long userId);

    List<Vault> findByUserIdAndWebsiteContainingIgnoreCase(Long userId, String website);

    List<Vault> findByUserIdAndUsernameContainingIgnoreCase(Long userId, String username);

    List<Vault> findByUserIdAndCategory(Long userId, String category);

    List<Vault> findByUserIdOrderByFavoriteDesc(Long userId);

    List<Vault> findByUserIdAndFavoriteTrue(Long userId);


}