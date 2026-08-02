package com.skillswap.service.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

/**
 * Stores password-reset OTPs in Redis with a short TTL — Redis expiring keys
 * are a natural fit for "this code is only valid for 10 minutes" and avoid
 * needing a dedicated DB table + cleanup job for something this ephemeral.
 */
@Service
@RequiredArgsConstructor
public class OtpService {

    private static final Duration OTP_TTL = Duration.ofMinutes(10);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final StringRedisTemplate redisTemplate;

    public String generateAndStore(String email) {
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        redisTemplate.opsForValue().set(key(email), otp, OTP_TTL);
        return otp;
    }

    public boolean verifyAndConsume(String email, String candidateOtp) {
        String key = key(email);
        String stored = redisTemplate.opsForValue().get(key);
        boolean matches = stored != null && stored.equals(candidateOtp);
        if (matches) {
            redisTemplate.delete(key); // one-time use
        }
        return matches;
    }

    private String key(String email) {
        return "otp:reset:" + email.toLowerCase();
    }
}
