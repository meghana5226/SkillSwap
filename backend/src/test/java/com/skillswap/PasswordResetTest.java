package com.skillswap;

import com.skillswap.dto.ForgotPasswordRequest;
import com.skillswap.dto.RegisterRequest;
import com.skillswap.dto.ResetPasswordRequest;
import com.skillswap.entity.Role;
import com.skillswap.entity.User;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.AuthService;
import com.skillswap.service.otp.OtpMailService;
import com.skillswap.service.otp.OtpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * OtpService/OtpMailService are mocked — they depend on Redis and real SMTP
 * respectively, neither of which is available in a plain unit-test run.
 * This verifies AuthService's business logic (who gets an OTP, what happens
 * on success/failure) independent of those infrastructure concerns.
 */
@SpringBootTest
@Transactional
class PasswordResetTest {

    @Autowired private AuthService authService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @MockBean private OtpService otpService;
    @MockBean private OtpMailService otpMailService;

    @Test
    void forgotPasswordSendsOtpOnlyForExistingUser() {
        String email = "reset.exists@example.com";
        authService.register(new RegisterRequest("Reset User", email, "StrongPass1!", Role.STUDENT));

        when(otpService.generateAndStore(email)).thenReturn("123456");

        authService.forgotPassword(new ForgotPasswordRequest(email));

        verify(otpService).generateAndStore(email);
        verify(otpMailService).sendOtp(eq(email), eq("123456"));
    }

    @Test
    void forgotPasswordDoesNothingForUnknownEmail() {
        authService.forgotPassword(new ForgotPasswordRequest("nobody.here@example.com"));

        verifyNoInteractions(otpService);
        verifyNoInteractions(otpMailService);
    }

    @Test
    void resetPasswordUpdatesPasswordOnValidOtp() {
        String email = "reset.valid@example.com";
        authService.register(new RegisterRequest("Reset Valid", email, "StrongPass1!", Role.STUDENT));

        when(otpService.verifyAndConsume(email, "111111")).thenReturn(true);

        authService.resetPassword(new ResetPasswordRequest(email, "111111", "NewStrongPass1!"));

        User updated = userRepository.findByEmail(email).orElseThrow();
        assertTrue(passwordEncoder.matches("NewStrongPass1!", updated.getPassword()));
    }

    @Test
    void resetPasswordRejectsInvalidOtp() {
        String email = "reset.invalid@example.com";
        authService.register(new RegisterRequest("Reset Invalid", email, "StrongPass1!", Role.STUDENT));

        when(otpService.verifyAndConsume(anyString(), anyString())).thenReturn(false);

        assertThrows(ApiException.class, () ->
                authService.resetPassword(new ResetPasswordRequest(email, "000000", "NewStrongPass1!")));
    }

    @Test
    void resetPasswordLocksOutAfterRepeatedFailures() {
        String email = "reset.lockout@example.com";
        authService.register(new RegisterRequest("Reset Lockout", email, "StrongPass1!", Role.STUDENT));

        when(otpService.verifyAndConsume(anyString(), anyString())).thenReturn(false);

        for (int i = 0; i < 5; i++) {
            assertThrows(ApiException.class, () ->
                    authService.resetPassword(new ResetPasswordRequest(email, "000000", "NewStrongPass1!")));
        }

        // 6th attempt should be blocked by the rate limiter itself.
        ApiException ex = assertThrows(ApiException.class, () ->
                authService.resetPassword(new ResetPasswordRequest(email, "000000", "NewStrongPass1!")));
        assertTrue(ex.getMessage().toLowerCase().contains("too many"));
    }
}
