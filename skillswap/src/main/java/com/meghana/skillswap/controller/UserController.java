package com.meghana.skillswap.controller;

import com.meghana.skillswap.dto.request.UpdateProfileRequest;
import com.meghana.skillswap.dto.response.UserResponse;

import com.meghana.skillswap.service.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse getCurrentUser(
            Authentication authentication
    ) {

        String email = authentication.getName();

        return userService.getCurrentUser(email);
    }

    @PutMapping("/profile")
    public UserResponse updateProfile(
            Authentication authentication,
            @Valid @RequestBody
            UpdateProfileRequest request
    ) {

        String email = authentication.getName();

        return userService.updateProfile(
                email,
                request
        );
    }
}