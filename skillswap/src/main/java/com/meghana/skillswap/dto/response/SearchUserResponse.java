package com.meghana.skillswap.dto.response;

import com.meghana.skillswap.entity.enums.SkillLevel;
import com.meghana.skillswap.entity.enums.SkillType;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserResponse {

    private Long userId;

    private String name;

    private String headline;

    private String location;

    private Double rating;

    private String profilePicture;

    private String skillName;

    private SkillLevel level;

    private SkillType type;
}