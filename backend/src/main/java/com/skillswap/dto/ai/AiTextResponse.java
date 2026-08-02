package com.skillswap.dto.ai;

/**
 * Generic wrapper for AI features that return a single block of Markdown
 * text (roadmap, resume review, interview tips, study plan, etc).
 */
public record AiTextResponse(String content) {
}
