package com.skillswap.dto;

import com.skillswap.entity.SessionStatus;

import java.time.Instant;
import java.util.UUID;

public record SessionResponse(
        UUID id,
        UUID requesterId,
        String requesterName,
        UUID mentorId,
        String mentorName,
        UUID skillId,
        String skillName,
        SessionStatus status,
        String message,
        Instant scheduledAt,
        Instant createdAt,
        boolean hasReview
) {
}
