package com.skillswap.repository;

import com.skillswap.entity.SessionRequest;
import com.skillswap.entity.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRequestRepository extends JpaRepository<SessionRequest, UUID> {

    List<SessionRequest> findByMentorIdOrderByCreatedAtDesc(UUID mentorId);

    List<SessionRequest> findByRequesterIdOrderByCreatedAtDesc(UUID requesterId);

    Optional<SessionRequest> findByRequesterIdAndMentorIdAndSkillIdAndStatus(
            UUID requesterId, UUID mentorId, UUID skillId, SessionStatus status);

    long countByStatus(SessionStatus status);
}
