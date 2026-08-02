package com.skillswap.dto;

import com.skillswap.entity.ProficiencyLevel;
import com.skillswap.entity.SkillType;

import java.util.UUID;

public record UserSkillResponse(
        UUID id,
        UUID skillId,
        String skillName,
        String category,
        SkillType type,
        ProficiencyLevel proficiency
) {
}
