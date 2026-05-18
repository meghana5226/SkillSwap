package com.meghana.skillswap.service;

import com.meghana.skillswap.dto.request.UpdateProfileRequest;
import com.meghana.skillswap.dto.response.UserResponse;

public interface UserService {

    UserResponse getCurrentUser(String email);

    UserResponse updateProfile(
            String email,
            UpdateProfileRequest request
    );
}