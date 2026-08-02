package com.skillswap.dto.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SkillGapRequest(

        @NotBlank(message = "Target role is required")
        @Size(max = 100)
        String targetRole // e.g. "Backend Developer", "Frontend Intern"
) {
}
