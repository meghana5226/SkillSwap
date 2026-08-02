package com.skillswap.dto;

import com.skillswap.entity.ProficiencyLevel;
import com.skillswap.entity.SkillType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddUserSkillRequest(

        @NotBlank(message = "Skill name is required")
        @Size(max = 100)
        String skillName,

        String category,

        @NotNull(message = "Type is required (OFFERING or LEARNING)")
        SkillType type,

        ProficiencyLevel proficiency
) {
}
