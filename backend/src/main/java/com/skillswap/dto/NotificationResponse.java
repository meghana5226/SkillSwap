package com.skillswap.dto;

import com.skillswap.entity.NotificationType;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        NotificationType type,
        String message,
        UUID relatedSessionId,
        boolean isRead,
        Instant createdAt
) {
}
