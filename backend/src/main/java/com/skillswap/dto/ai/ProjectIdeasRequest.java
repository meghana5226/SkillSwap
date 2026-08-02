package com.skillswap.dto.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectIdeasRequest(

        @NotBlank(message = "Skill is required")
        @Size(max = 100)
        String skill,

        @Size(max = 50)
        String level // optional, e.g. "beginner"
) {
}
