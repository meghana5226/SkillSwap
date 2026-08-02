package com.skillswap.dto;

import com.skillswap.entity.ProficiencyLevel;

import java.util.UUID;

public record MentorSearchResult(
        UUID userId,
        String fullName,
        String bio,
        String experienceLevel,
        String location,
        boolean available,
        UUID skillId,
        String skillName,
        ProficiencyLevel proficiency,
        double averageRating,
        long reviewCount
) {
}
