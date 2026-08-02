package com.skillswap.service;

import com.skillswap.dto.NotificationResponse;
import com.skillswap.entity.Notification;
import com.skillswap.entity.NotificationType;
import com.skillswap.entity.User;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Called from SessionService/ReviewService on lifecycle events. Kept
     * best-effort on purpose — a notification failing to save should never
     * roll back or block the actual session/review action that triggered it.
     */
    @Transactional
    public void notify(User recipient, NotificationType type, String message, UUID relatedSessionId) {
        Notification notification = Notification.builder()
                .user(recipient)
                .type(type)
                .message(message)
                .relatedSessionId(relatedSessionId)
                .build();
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> listMine(UUID userId) {
        return notificationRepository.findTop50ByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public long unreadCount(UUID userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markRead(UUID userId, UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ApiException("Notification not found", HttpStatus.NOT_FOUND));
        if (!notification.getUser().getId().equals(userId)) {
            throw new ApiException("You can only manage your own notifications", HttpStatus.FORBIDDEN);
        }
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllRead(UUID userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndIsReadFalse(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(), n.getType(), n.getMessage(), n.getRelatedSessionId(), n.isRead(), n.getCreatedAt()
        );
    }
}
