package com.meghana.skillswap.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SendRequestDto {

    @NotNull
    private Long userSkillId;

    private String message;
}