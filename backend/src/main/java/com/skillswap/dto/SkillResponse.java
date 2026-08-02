package com.skillswap.dto;

import java.util.UUID;

public record SkillResponse(UUID id, String name, String category) {
}
