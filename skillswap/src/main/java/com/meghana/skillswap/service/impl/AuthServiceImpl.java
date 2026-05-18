package com.meghana.skillswap.service.impl;

import com.meghana.skillswap.dto.auth.AuthResponse;
import com.meghana.skillswap.dto.auth.LoginRequest;
import com.meghana.skillswap.dto.auth.RegisterRequest;

import com.meghana.skillswap.entity.User;
import com.meghana.skillswap.entity.enums.Role;

import com.meghana.skillswap.repository.UserRepository;

import com.meghana.skillswap.security.JwtService;

import com.meghana.skillswap.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .location(request.getLocation())
                .bio(request.getBio())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token =
                jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token =
                jwtService.generateToken(
                        request.getEmail()
                );

        return new AuthResponse(token);
    }
}