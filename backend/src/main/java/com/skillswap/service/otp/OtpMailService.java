package com.skillswap.service.otp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Sends the password-reset OTP by email. If no SMTP credentials are
 * configured (the out-of-the-box local/demo state), it logs the OTP
 * instead of failing — so the whole reset flow is testable without
 * setting up a real mail account.
 */
@Slf4j
@Service
public class OtpMailService {

    private final JavaMailSender mailSender;
    private final String mailUsername;
    private final String fromAddress;

    public OtpMailService(
            JavaMailSender mailSender,
            @Value("${spring.mail.username:}") String mailUsername,
            @Value("${spring.mail.username:no-reply@skillswap.dev}") String fromAddress) {
        this.mailSender = mailSender;
        this.mailUsername = mailUsername;
        this.fromAddress = fromAddress;
    }

    public void sendOtp(String toEmail, String otp) {
        if (mailUsername == null || mailUsername.isBlank()) {
            // Dev/demo fallback: no SMTP configured, so surface the OTP in
            // the server logs instead of silently failing.
            log.info("MAIL_USERNAME not configured — password reset OTP for {} is: {}", toEmail, otp);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toEmail);
            message.setSubject("Your SkillSwap AI password reset code");
            message.setText("Your password reset code is: " + otp + "\n\nThis code expires in 10 minutes. " +
                    "If you didn't request this, you can safely ignore this email.");
            mailSender.send(message);
        } catch (Exception e) {
            // Don't fail the request just because email delivery had an issue —
            // log the OTP so the flow is still usable, and log the error for ops.
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
            log.info("Password reset OTP for {} is: {}", toEmail, otp);
        }
    }
}
