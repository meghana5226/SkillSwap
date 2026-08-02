package com.skillswap.service;

import com.skillswap.dto.CreateSessionRequest;
import com.skillswap.dto.SessionResponse;
import com.skillswap.entity.*;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRequestRepository sessionRequestRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final ReviewRepository reviewRepository;
    private final NotificationService notificationService;

    @Transactional
    public SessionResponse createRequest(String requesterEmail, CreateSessionRequest request) {
        User requester = findUser(requesterEmail);
        User mentor = userRepository.findById(request.mentorId())
                .orElseThrow(() -> new ApiException("Mentor not found", HttpStatus.NOT_FOUND));
        Skill skill = skillRepository.findById(request.skillId())
                .orElseThrow(() -> new ApiException("Skill not found", HttpStatus.NOT_FOUND));

        if (requester.getId().equals(mentor.getId())) {
            throw new ApiException("You can't request a session with yourself", HttpStatus.BAD_REQUEST);
        }

        boolean mentorOffersSkill = userSkillRepository
                .findByUserIdAndSkillIdAndType(mentor.getId(), skill.getId(), SkillType.OFFERING)
                .isPresent();
        if (!mentorOffersSkill) {
            throw new ApiException("This mentor doesn't currently offer that skill", HttpStatus.BAD_REQUEST);
        }

        sessionRequestRepository
                .findByRequesterIdAndMentorIdAndSkillIdAndStatus(
                        requester.getId(), mentor.getId(), skill.getId(), SessionStatus.PENDING)
                .ifPresent(existing -> {
                    throw new ApiException(
                            "You already have a pending request for this skill with this mentor",
                            HttpStatus.CONFLICT
                    );
                });

        SessionRequest session = SessionRequest.builder()
                .requester(requester)
                .mentor(mentor)
                .skill(skill)
                .message(request.message())
                .scheduledAt(request.scheduledAt())
                .status(SessionStatus.PENDING)
                .build();

        sessionRequestRepository.save(session);
        notificationService.notify(
                mentor, NotificationType.SESSION_REQUESTED,
                requester.getFullName() + " requested a session for " + skill.getName(),
                session.getId()
        );
        return toResponse(session);
    }

    @Transactional(readOnly = true)
    public List<SessionResponse> getIncoming(String mentorEmail) {
        User mentor = findUser(mentorEmail);
        return sessionRequestRepository.findByMentorIdOrderByCreatedAtDesc(mentor.getId())
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<SessionResponse> getOutgoing(String requesterEmail) {
        User requester = findUser(requesterEmail);
        return sessionRequestRepository.findByRequesterIdOrderByCreatedAtDesc(requester.getId())
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public SessionResponse accept(String mentorEmail, UUID sessionId) {
        SessionRequest session = getOwnedByMentor(mentorEmail, sessionId);
        requireStatus(session, SessionStatus.PENDING, "accepted");
        session.setStatus(SessionStatus.ACCEPTED);
        sessionRequestRepository.save(session);
        notificationService.notify(
                session.getRequester(), NotificationType.SESSION_ACCEPTED,
                session.getMentor().getFullName() + " accepted your request for " + session.getSkill().getName(),
                session.getId()
        );
        return toResponse(session);
    }

    @Transactional
    public SessionResponse reject(String mentorEmail, UUID sessionId) {
        SessionRequest session = getOwnedByMentor(mentorEmail, sessionId);
        requireStatus(session, SessionStatus.PENDING, "rejected");
        session.setStatus(SessionStatus.REJECTED);
        sessionRequestRepository.save(session);
        notificationService.notify(
                session.getRequester(), NotificationType.SESSION_REJECTED,
                session.getMentor().getFullName() + " declined your request for " + session.getSkill().getName(),
                session.getId()
        );
        return toResponse(session);
    }

    @Transactional
    public SessionResponse complete(String mentorEmail, UUID sessionId) {
        SessionRequest session = getOwnedByMentor(mentorEmail, sessionId);
        requireStatus(session, SessionStatus.ACCEPTED, "completed");
        session.setStatus(SessionStatus.COMPLETED);
        sessionRequestRepository.save(session);
        notificationService.notify(
                session.getRequester(), NotificationType.SESSION_COMPLETED,
                "Your session with " + session.getMentor().getFullName() + " for " + session.getSkill().getName() + " is complete — leave a review!",
                session.getId()
        );
        return toResponse(session);
    }

    @Transactional
    public SessionResponse cancel(String requesterEmail, UUID sessionId) {
        User requester = findUser(requesterEmail);
        SessionRequest session = sessionRequestRepository.findById(sessionId)
                .orElseThrow(() -> new ApiException("Session request not found", HttpStatus.NOT_FOUND));

        if (!session.getRequester().getId().equals(requester.getId())) {
            throw new ApiException("You can only cancel your own requests", HttpStatus.FORBIDDEN);
        }
        requireStatus(session, SessionStatus.PENDING, "cancelled");

        session.setStatus(SessionStatus.CANCELLED);
        sessionRequestRepository.save(session);
        return toResponse(session);
    }

    private SessionRequest getOwnedByMentor(String mentorEmail, UUID sessionId) {
        User mentor = findUser(mentorEmail);
        SessionRequest session = sessionRequestRepository.findById(sessionId)
                .orElseThrow(() -> new ApiException("Session request not found", HttpStatus.NOT_FOUND));

        if (!session.getMentor().getId().equals(mentor.getId())) {
            throw new ApiException("You can only manage requests sent to you", HttpStatus.FORBIDDEN);
        }
        return session;
    }

    private void requireStatus(SessionRequest session, SessionStatus required, String actionPastTense) {
        if (session.getStatus() != required) {
            throw new ApiException(
                    "This request can't be " + actionPastTense + " from its current status (" + session.getStatus() + ")",
                    HttpStatus.CONFLICT
            );
        }
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }

    private SessionResponse toResponse(SessionRequest s) {
        return new SessionResponse(
                s.getId(),
                s.getRequester().getId(),
                s.getRequester().getFullName(),
                s.getMentor().getId(),
                s.getMentor().getFullName(),
                s.getSkill().getId(),
                s.getSkill().getName(),
                s.getStatus(),
                s.getMessage(),
                s.getScheduledAt(),
                s.getCreatedAt(),
                reviewRepository.existsBySessionId(s.getId())
        );
    }
}
