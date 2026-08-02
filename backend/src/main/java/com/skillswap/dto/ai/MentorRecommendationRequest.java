package com.skillswap.dto.ai;

import jakarta.validation.constraints.Size;

public record MentorRecommendationRequest(

        // Optional — if omitted, we use one of the user's "LEARNING" skills instead.
        @Size(max = 100)
        String skill
) {
}
