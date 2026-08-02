package com.skillswap.dto.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResumeReviewRequest(

        @NotBlank(message = "Resume text is required")
        @Size(max = 20000, message = "Resume text is too long")
        String resumeText
) {
}
