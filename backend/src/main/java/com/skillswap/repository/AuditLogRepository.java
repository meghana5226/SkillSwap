package com.skillswap.repository;

import com.skillswap.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, java.util.UUID> {

    List<AuditLog> findTop100ByOrderByCreatedAtDesc();
}
