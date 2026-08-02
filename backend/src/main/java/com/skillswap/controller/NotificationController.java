package com.skillswap.controller;

import com.skillswap.dto.NotificationResponse;
import com.skillswap.entity.User;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "In-app notifications for session/review activity")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "List my most recent notifications")
    public ResponseEntity<List<NotificationResponse>> list(Authentication authentication) {
        UUID userId = currentUserId(authentication);
        return ResponseEntity.ok(notificationService.listMine(userId));
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get my unread notification count")
    public ResponseEntity<Map<String, Long>> unreadCount(Authentication authentication) {
        UUID userId = currentUserId(authentication);
        return ResponseEntity.ok(Map.of("count", notificationService.unreadCount(userId)));
    }

    @PostMapping("/{id}/read")
    @Operation(summary = "Mark one notification as read")
    public ResponseEntity<Void> markRead(Authentication authentication, @PathVariable UUID id) {
        notificationService.markRead(currentUserId(authentication), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/read-all")
    @Operation(summary = "Mark all my notifications as read")
    public ResponseEntity<Void> markAllRead(Authentication authentication) {
        notificationService.markAllRead(currentUserId(authentication));
        return ResponseEntity.noContent().build();
    }

    private UUID currentUserId(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return user.getId();
    }
}
