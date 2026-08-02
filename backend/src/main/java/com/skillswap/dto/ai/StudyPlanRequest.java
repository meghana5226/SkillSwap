package com.skillswap.dto.ai;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StudyPlanRequest(

        @NotBlank(message = "Skill is required")
        @Size(max = 100)
        String skill,

        @Min(value = 1, message = "Hours per week must be at least 1")
        @Max(value = 60, message = "Hours per week must be realistic (max 60)")
        int hoursPerWeek
) {
}
