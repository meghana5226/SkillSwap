package com.skillswap.dto.ai;

import com.skillswap.dto.MentorSearchResult;

import java.util.List;

public record MentorRecommendationResponse(
        String recommendation,
        List<MentorSearchResult> candidates
) {
}
