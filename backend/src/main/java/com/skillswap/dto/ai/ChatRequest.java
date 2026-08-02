package com.skillswap.dto.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ChatRequest(

        @NotBlank(message = "Message is required")
        @Size(max = 2000)
        String message,

        // Prior turns of the conversation, oldest first. Optional — a fresh
        // conversation can omit it. Kept small on the client side to avoid
        // unbounded prompt growth.
        List<ChatTurn> history
) {
    public record ChatTurn(String role, String content) {
        // role is "user" or "assistant"
    }
}
