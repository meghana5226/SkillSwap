package com.skillswap.controller;

import com.skillswap.dto.ReviewRequest;
import com.skillswap.dto.ReviewResponse;
import com.skillswap.service.ReviewService;
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
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Rate a mentor after a completed session")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/api/sessions/{sessionId}/review")
    @Operation(summary = "Submit a review for a completed session (requester only)")
    public ResponseEntity<ReviewResponse> submitReview(
            Authentication authentication,
            @PathVariable UUID sessionId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.submitReview(authentication.getName(), sessionId, request));
    }

    @GetMapping("/api/mentors/{mentorId}/reviews")
    @Operation(summary = "List reviews for a mentor")
    public ResponseEntity<List<ReviewResponse>> getMentorReviews(@PathVariable UUID mentorId) {
        return ResponseEntity.ok(reviewService.getReviewsForMentor(mentorId));
    }
}
