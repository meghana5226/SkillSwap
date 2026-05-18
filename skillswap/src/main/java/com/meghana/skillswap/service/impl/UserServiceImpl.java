package com.meghana.skillswap.service.impl;

import com.meghana.skillswap.dto.request.UpdateProfileRequest;
import com.meghana.skillswap.dto.response.UserResponse;

import com.meghana.skillswap.entity.User;

import com.meghana.skillswap.repository.UserRepository;

import com.meghana.skillswap.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getCurrentUser(String email) {

        User user = getUserByEmail(email);

        return mapToResponse(user);
    }

    @Override
    public UserResponse updateProfile(
            String email,
            UpdateProfileRequest request
    ) {

        User user = getUserByEmail(email);

        user.setHeadline(request.getHeadline());
        user.setBio(request.getBio());
        user.setLocation(request.getLocation());
        user.setProfilePicture(
                request.getProfilePicture()
        );

        userRepository.save(user);

        return mapToResponse(user);
    }

    private User getUserByEmail(String email) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));
    }

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .location(user.getLocation())
                .bio(user.getBio())
                .profilePicture(
                        user.getProfilePicture()
                )
                .headline(user.getHeadline())
                .rating(user.getRating())
                .role(user.getRole().name())
                .build();
    }
}