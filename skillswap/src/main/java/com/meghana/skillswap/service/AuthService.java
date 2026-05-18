package com.meghana.skillswap.service;

import com.meghana.skillswap.dto.auth.AuthResponse;
import com.meghana.skillswap.dto.auth.LoginRequest;
import com.meghana.skillswap.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}