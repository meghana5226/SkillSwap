package com.skillswap.dto.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoadmapRequest(

        @NotBlank(message = "Target skill is required")
        @Size(max = 100)
        String targetSkill,

        @Size(max = 50)
        String currentLevel // e.g. "complete beginner", "know basics", optional
) {
}
