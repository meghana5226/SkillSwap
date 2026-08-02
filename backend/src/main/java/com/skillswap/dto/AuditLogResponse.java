package com.skillswap.dto;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResponse(
        UUID id,
        String actorName,
        String actorEmail,
        String action,
        String targetType,
        UUID targetId,
        String details,
        Instant createdAt
) {
}
