package com.meghana.skillswap.dto.request;

import com.meghana.skillswap.entity.enums.SkillLevel;
import com.meghana.skillswap.entity.enums.SkillType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AddSkillRequest {

    @NotBlank(message = "Skill name is required")
    private String skillName;

    @NotNull(message = "Skill type is required")
    private SkillType type;

    @NotNull(message = "Skill level is required")
    private SkillLevel level;

    @Size(max = 500)
    private String description;
}