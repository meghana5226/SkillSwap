package com.meghana.skillswap.repository;

import com.meghana.skillswap.entity.SkillRequest;
import com.meghana.skillswap.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRequestRepository
        extends JpaRepository<SkillRequest, Long> {

    List<SkillRequest> findByReceiver(User receiver);

    List<SkillRequest> findBySender(User sender);

    long countBySender_Email(String email);

    long countByReceiver_Email(String email);

    long countByReceiver_EmailAndStatus(
            String email,
            com.meghana.skillswap.entity.enums.RequestStatus status
    );
}