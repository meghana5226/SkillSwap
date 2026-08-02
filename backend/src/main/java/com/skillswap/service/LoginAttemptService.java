package com.skillswap.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lightweight brute-force protection: locks an email out after
 * MAX_ATTEMPTS failed logins for LOCKOUT_MINUTES.
 *
 * This is in-memory and per-instance, which is fine for a single backend
 * instance / demo deployment. For multi-instance production use, back
 * this with Redis (increment + TTL) instead of a ConcurrentHashMap.
 */
@Service
public class LoginAttemptService {

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
        if (attempt.lockedUntil() != null && !attempt.lockedUntil().isAfter(Instant.now())) {
            attempts.remove(email); // lockout expired, reset
        }
        return false;
    }

    public void recordFailure(String email) {
        attempts.compute(email, (key, current) -> {
            int count = (current == null ? 0 : current.count()) + 1;
            Instant lockedUntil = count >= MAX_ATTEMPTS
                    ? Instant.now().plusSeconds(LOCKOUT_MINUTES * 60)
                    : null;
            return new Attempt(count, lockedUntil);
        });
    }

    public void recordSuccess(String email) {
        attempts.remove(email);
    }
}
