package com.skillswap.controller;

import com.skillswap.dto.CreateSessionRequest;
import com.skillswap.dto.SessionResponse;
import com.skillswap.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Tag(name = "Sessions", description = "Skill-exchange session requests: request, accept, reject, complete, cancel")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    @Operation(summary = "Request a skill-exchange session with a mentor")
    public ResponseEntity<SessionResponse> create(
            Authentication authentication,
            @Valid @RequestBody CreateSessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.createRequest(authentication.getName(), request));
    }

    @GetMapping("/incoming")
    @Operation(summary = "Requests sent to me as a mentor")
    public ResponseEntity<List<SessionResponse>> incoming(Authentication authentication) {
        return ResponseEntity.ok(sessionService.getIncoming(authentication.getName()));
    }

    @GetMapping("/outgoing")
    @Operation(summary = "Requests I've sent as a learner")
    public ResponseEntity<List<SessionResponse>> outgoing(Authentication authentication) {
        return ResponseEntity.ok(sessionService.getOutgoing(authentication.getName()));
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept a pending request (mentor only)")
    public ResponseEntity<SessionResponse> accept(Authentication authentication, @PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.accept(authentication.getName(), id));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject a pending request (mentor only)")
    public ResponseEntity<SessionResponse> reject(Authentication authentication, @PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.reject(authentication.getName(), id));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Mark an accepted session as completed (mentor only)")
    public ResponseEntity<SessionResponse> complete(Authentication authentication, @PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.complete(authentication.getName(), id));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a pending request (requester only)")
    public ResponseEntity<SessionResponse> cancel(Authentication authentication, @PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.cancel(authentication.getName(), id));
    }
}
