package com.skillswap.dto;

import com.skillswap.entity.Role;

import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String fullName,
        String email,
        Role role,
        String accessToken,
        String refreshToken,
        String tokenType
) {
    public AuthResponse(UUID userId, String fullName, String email, Role role,
                         String accessToken, String refreshToken) {
        this(userId, fullName, email, role, accessToken, refreshToken, "Bearer");
    }
}
