package com.skillswap.dto;

import java.time.Instant;
import java.util.UUID;

public record BookmarkResponse(
        UUID id,
        UUID bookmarkedUserId,
        String bookmarkedUserName,
        String bookmarkedUserRole,
        Instant createdAt
) {
}
