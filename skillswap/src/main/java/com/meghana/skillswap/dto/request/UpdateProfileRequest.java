package com.meghana.skillswap.dto.request;

import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(max = 100)
    private String headline;

    @Size(max = 1000)
    private String bio;

    @Size(max = 100)
    private String location;

    private String profilePicture;
}