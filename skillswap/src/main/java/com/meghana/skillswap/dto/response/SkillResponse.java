package com.meghana.skillswap.dto.response;

import com.meghana.skillswap.entity.enums.SkillLevel;
import com.meghana.skillswap.entity.enums.SkillType;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponse {

    private Long id;

    private String skillName;

    private SkillType type;

    private SkillLevel level;

    private String description;
}