package com.skillswap.service;

import com.skillswap.dto.AdminStatsResponse;
import com.skillswap.dto.AdminUserResponse;
import com.skillswap.entity.Role;
import com.skillswap.entity.SessionStatus;
import com.skillswap.entity.User;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.ReviewRepository;
import com.skillswap.repository.SessionRequestRepository;
import com.skillswap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final SessionRequestRepository sessionRequestRepository;
    private final ReviewRepository reviewRepository;
    private final AuditLogService auditLogService;

    @Transactional(readOnly = true)
    public List<AdminUserResponse> listUsers() {
        return userRepository.findAll().stream().map(this::toAdminUserResponse).toList();
    }

    @Transactional
    public AdminUserResponse setUserEnabled(String adminEmail, UUID targetUserId, boolean enabled) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ApiException("Admin not found", HttpStatus.NOT_FOUND));
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        if (target.getId().equals(admin.getId())) {
            throw new ApiException("You can't change your own account status", HttpStatus.BAD_REQUEST);
        }

        target.setEnabled(enabled);
        userRepository.save(target);

        auditLogService.record(
                admin,
                enabled ? "USER_ENABLED" : "USER_DISABLED",
                "User",
                target.getId(),
                "Target: " + target.getEmail()
        );

        return toAdminUserResponse(target);
    }

    @Transactional(readOnly = true)
    public AdminStatsResponse getStats() {
        Double avgRating = reviewRepository.averageRating();

        return new AdminStatsResponse(
                userRepository.count(),
                userRepository.countByRole(Role.STUDENT),
                userRepository.countByRole(Role.MENTOR),
                userRepository.countByRole(Role.ADMIN),
                sessionRequestRepository.count(),
                sessionRequestRepository.countByStatus(SessionStatus.PENDING),
                sessionRequestRepository.countByStatus(SessionStatus.COMPLETED),
                reviewRepository.count(),
                avgRating == null ? 0.0 : Math.round(avgRating * 10) / 10.0
        );
    }

    private AdminUserResponse toAdminUserResponse(User u) {
        return new AdminUserResponse(
                u.getId(), u.getFullName(), u.getEmail(), u.getRole(),
                u.isEnabled(), u.isEmailVerified(), u.getCreatedAt()
        );
    }
}
