package com.skillswap.dto;

import com.skillswap.entity.Role;

import java.time.Instant;
import java.util.UUID;

public record AdminUserResponse(
        UUID id,
        String fullName,
        String email,
        Role role,
        boolean enabled,
        boolean emailVerified,
        Instant createdAt
) {
}
