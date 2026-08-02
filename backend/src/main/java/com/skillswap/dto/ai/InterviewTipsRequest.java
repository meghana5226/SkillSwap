package com.skillswap.dto.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InterviewTipsRequest(

        @NotBlank(message = "Skill/topic is required")
        @Size(max = 100)
        String skill
) {
}
