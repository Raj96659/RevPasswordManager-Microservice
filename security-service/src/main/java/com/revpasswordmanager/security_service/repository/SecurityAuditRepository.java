package com.revpasswordmanager.security_service.repository;

import com.revpasswordmanager.security_service.entity.SecurityAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityAuditRepository
        extends JpaRepository<SecurityAudit, Long> {

    List<SecurityAudit> findByUserId(Long userId);

}