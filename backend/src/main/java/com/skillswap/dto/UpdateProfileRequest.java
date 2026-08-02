package com.skillswap.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @Size(max = 1000, message = "Bio must be under 1000 characters")
        String bio,

        @Size(max = 50)
        String experienceLevel,

        @Pattern(regexp = "^(https?://.*)?$", message = "Must be a valid URL")
        String githubUrl,

        @Pattern(regexp = "^(https?://.*)?$", message = "Must be a valid URL")
        String linkedinUrl,

        @Pattern(regexp = "^(https?://.*)?$", message = "Must be a valid URL")
        String portfolioUrl,

        @Size(max = 150)
        String location,

        Boolean available
) {
}
