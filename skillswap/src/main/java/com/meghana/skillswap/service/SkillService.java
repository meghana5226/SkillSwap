package com.meghana.skillswap.service;

import com.meghana.skillswap.dto.request.AddSkillRequest;
import com.meghana.skillswap.dto.response.SkillResponse;

import java.util.List;

public interface SkillService {

    SkillResponse addSkill(
            String email,
            AddSkillRequest request
    );

    List<SkillResponse> getMySkills(
            String email
    );

    void deleteSkill(
            Long skillId,
            String email
    );
}