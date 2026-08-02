package com.skillswap;

import com.skillswap.dto.LoginRequest;
import com.skillswap.dto.RegisterRequest;
import com.skillswap.entity.Role;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerCreatesUserAndReturnsTokens() {
        var request = new RegisterRequest("Asha Verma", "asha@example.com", "StrongPass1!", Role.STUDENT);

        var response = authService.register(request);

        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());
        assertEquals("asha@example.com", response.email());
        assertTrue(userRepository.existsByEmail("asha@example.com"));
    }

    @Test
    void registerRejectsDuplicateEmail() {
        var request = new RegisterRequest("Asha Verma", "dup@example.com", "StrongPass1!", Role.STUDENT);
        authService.register(request);

        var duplicate = new RegisterRequest("Another Name", "dup@example.com", "StrongPass1!", Role.STUDENT);

        assertThrows(ApiException.class, () -> authService.register(duplicate));
    }

    @Test
    void loginFailsWithWrongPassword() {
        authService.register(new RegisterRequest("Ravi Kumar", "ravi@example.com", "StrongPass1!", Role.STUDENT));

        var badLogin = new LoginRequest("ravi@example.com", "WrongPassword1!");

        assertThrows(BadCredentialsException.class, () -> authService.login(badLogin));
    }

    @Test
    void loginSucceedsWithCorrectCredentials() {
        authService.register(new RegisterRequest("Meera Nair", "meera@example.com", "StrongPass1!", Role.STUDENT));

        var response = authService.login(new LoginRequest("meera@example.com", "StrongPass1!"));

        assertNotNull(response.accessToken());
        assertEquals("meera@example.com", response.email());
    }
}
