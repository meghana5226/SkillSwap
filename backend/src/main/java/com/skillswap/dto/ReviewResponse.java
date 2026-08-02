package com.skillswap.dto;

import java.time.Instant;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        UUID sessionId,
        UUID reviewerId,
        String reviewerName,
        int rating,
        String comment,
        Instant createdAt
) {
}
