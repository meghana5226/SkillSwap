package com.skillswap.service;

import com.skillswap.dto.ReviewRequest;
import com.skillswap.dto.ReviewResponse;
import com.skillswap.entity.NotificationType;
import com.skillswap.entity.Review;
import com.skillswap.entity.SessionRequest;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SessionRequestRepository sessionRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public ReviewResponse submitReview(String reviewerEmail, UUID sessionId, ReviewRequest request) {
        User reviewer = userRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        SessionRequest session = sessionRequestRepository.findById(sessionId)
                .orElseThrow(() -> new ApiException("Session request not found", HttpStatus.NOT_FOUND));

        if (!session.getRequester().getId().equals(reviewer.getId())) {
            throw new ApiException("Only the person who requested the session can review it", HttpStatus.FORBIDDEN);
        }
        if (session.getStatus() != SessionStatus.COMPLETED) {
            throw new ApiException("You can only review a completed session", HttpStatus.CONFLICT);
        }
        if (reviewRepository.existsBySessionId(sessionId)) {
            throw new ApiException("This session has already been reviewed", HttpStatus.CONFLICT);
        }

        Review review = Review.builder()
                .session(session)
                .reviewer(reviewer)
                .mentor(session.getMentor())
                .rating(request.rating())
                .comment(request.comment())
                .build();

        reviewRepository.save(review);
        notificationService.notify(
                session.getMentor(), NotificationType.REVIEW_RECEIVED,
                reviewer.getFullName() + " left you a " + request.rating() + "-star review",
                session.getId()
        );
        return toResponse(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsForMentor(UUID mentorId) {
        return reviewRepository.findByMentorIdOrderByCreatedAtDesc(mentorId)
                .stream().map(this::toResponse).toList();
    }

    private ReviewResponse toResponse(Review r) {
        return new ReviewResponse(
                r.getId(),
                r.getSession().getId(),
                r.getReviewer().getId(),
                r.getReviewer().getFullName(),
                r.getRating(),
                r.getComment(),
                r.getCreatedAt()
        );
    }
}
