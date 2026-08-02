package com.skillswap.service;

import com.skillswap.dto.AuthResponse;
import com.skillswap.dto.ForgotPasswordRequest;
import com.skillswap.dto.LoginRequest;
import com.skillswap.dto.RefreshRequest;
import com.skillswap.dto.RegisterRequest;
import com.skillswap.dto.ResetPasswordRequest;
import com.skillswap.entity.Role;
import com.skillswap.entity.User;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.UserRepository;
import com.skillswap.security.CustomUserDetailsService;
import com.skillswap.security.JwtService;
import com.skillswap.service.otp.OtpAttemptService;
import com.skillswap.service.otp.OtpMailService;
import com.skillswap.service.otp.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;
    private final OtpService otpService;
    private final OtpAttemptService otpAttemptService;
    private final OtpMailService otpMailService;

    public AuthResponse register(RegisterRequest request) {
        // Prevent duplicate accounts.
        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException("An account with this email already exists", HttpStatus.CONFLICT);
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email().toLowerCase())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() != null ? request.role() : Role.STUDENT)
                .build();

        userRepository.save(user);

        return issueTokens(user);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().toLowerCase();

        if (loginAttemptService.isBlocked(email)) {
            throw new ApiException(
                    "Too many failed login attempts. Please try again in a few minutes.",
                    HttpStatus.TOO_MANY_REQUESTS
            );
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.password())
            );
        } catch (BadCredentialsException ex) {
            loginAttemptService.recordFailure(email);
            throw ex;
        }

        loginAttemptService.recordSuccess(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        return issueTokens(user);
    }

    public AuthResponse refresh(RefreshRequest request) {
        String token = request.refreshToken();
        String email;
        try {
            email = jwtService.extractEmail(token);
        } catch (Exception ex) {
            throw new ApiException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        if (!jwtService.isRefreshToken(token)) {
            throw new ApiException("Provided token is not a refresh token", HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new ApiException("Refresh token expired or invalid", HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        return issueTokens(user);
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        String email = request.email().toLowerCase();

        // Deliberately don't reveal whether the account exists — same response either way.
        userRepository.findByEmail(email).ifPresent(user -> {
            String otp = otpService.generateAndStore(email);
            otpMailService.sendOtp(email, otp);
        });
    }

    public void resetPassword(ResetPasswordRequest request) {
        String email = request.email().toLowerCase();

        if (otpAttemptService.isBlocked(email)) {
            throw new ApiException(
                    "Too many incorrect codes. Please request a new one in a few minutes.",
                    HttpStatus.TOO_MANY_REQUESTS
            );
        }

        boolean valid = otpService.verifyAndConsume(email, request.otp());
        if (!valid) {
            otpAttemptService.recordFailure(email);
            throw new ApiException("That code is invalid or has expired", HttpStatus.BAD_REQUEST);
        }
        otpAttemptService.recordSuccess(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private AuthResponse issueTokens(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                accessToken,
                refreshToken
        );
    }
}
