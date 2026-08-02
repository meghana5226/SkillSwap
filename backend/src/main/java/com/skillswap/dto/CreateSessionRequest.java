package com.skillswap.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record CreateSessionRequest(

        @NotNull(message = "Mentor is required")
        UUID mentorId,

        @NotNull(message = "Skill is required")
        UUID skillId,

        @Size(max = 500)
        String message,

        Instant scheduledAt
) {
}
