package com.skillswap.service.otp;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Same in-memory-per-instance pattern as LoginAttemptService — see that
 * class for the note on backing this with Redis for multi-instance
 * deployments. A 6-digit OTP has a million combinations; without this,
 * someone could brute-force it in seconds.
 */
@Service
public class OtpAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCKOUT_MINUTES = 15;

    private record Attempt(int count, Instant lockedUntil) {}

    private final ConcurrentHashMap<String, Attempt> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String email) {
        Attempt attempt = attempts.get(email);
        if (attempt == null) return false;
        if (attempt.lockedUntil() != null && attempt.lockedUntil().isAfter(Instant.now())) {
            return true;
        }
        if (attempt.lockedUntil() != null) {
            attempts.remove(email);
        }
        return false;
    }

    public void recordFailure(String email) {
        attempts.compute(email, (key, current) -> {
            int count = (current == null ? 0 : current.count()) + 1;
            Instant lockedUntil = count >= MAX_ATTEMPTS ? Instant.now().plusSeconds(LOCKOUT_MINUTES * 60) : null;
            return new Attempt(count, lockedUntil);
        });
    }

    public void recordSuccess(String email) {
        attempts.remove(email);
    }
}
