package com.skillswap.controller;

import com.skillswap.dto.*;
import com.skillswap.service.AdminService;
import com.skillswap.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Every endpoint here is restricted to ADMIN by SecurityConfig's
 * "/api/admin/**" -> hasRole("ADMIN") rule — no per-method @PreAuthorize
 * needed, but that global rule is the thing actually enforcing it.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "User management, platform stats, and audit log (admin-only)")
public class AdminController {

    private final AdminService adminService;
    private final AuditLogService auditLogService;

    @GetMapping("/users")
    @Operation(summary = "List all users")
    public ResponseEntity<List<AdminUserResponse>> listUsers() {
        return ResponseEntity.ok(adminService.listUsers());
    }

    @PatchMapping("/users/{userId}/status")
    @Operation(summary = "Enable or disable a user account")
    public ResponseEntity<AdminUserResponse> setUserStatus(
            Authentication authentication,
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        return ResponseEntity.ok(adminService.setUserEnabled(authentication.getName(), userId, request.enabled()));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get platform-wide stats")
    public ResponseEntity<AdminStatsResponse> stats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/audit-logs")
    @Operation(summary = "List the 100 most recent audit log entries")
    public ResponseEntity<List<AuditLogResponse>> auditLogs() {
        return ResponseEntity.ok(auditLogService.recent());
    }
}
