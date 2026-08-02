package com.skillswap.dto;

import com.skillswap.entity.Role;

import java.util.List;
import java.util.UUID;

public record ProfileResponse(
        UUID id,
        String fullName,
        String email,
        Role role,
        String bio,
        String experienceLevel,
        String githubUrl,
        String linkedinUrl,
        String portfolioUrl,
        String resumeUrl,
        String location,
        boolean available,
        List<UserSkillResponse> skills
) {
}
