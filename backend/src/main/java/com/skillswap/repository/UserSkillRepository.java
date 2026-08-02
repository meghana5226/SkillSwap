package com.skillswap.repository;

import com.skillswap.entity.SkillType;
import com.skillswap.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSkillRepository extends JpaRepository<UserSkill, UUID> {

    List<UserSkill> findByUserId(UUID userId);

    Optional<UserSkill> findByUserIdAndSkillIdAndType(UUID userId, UUID skillId, SkillType type);

    Optional<UserSkill> findByIdAndUserId(UUID id, UUID userId);

    // Used later by mentor-matching: find people OFFERING a skill someone else is LEARNING.
    List<UserSkill> findBySkillIdAndType(UUID skillId, SkillType type);

    // Mentor search: find everyone OFFERING a skill whose name matches the query.
    List<UserSkill> findByTypeAndSkill_NameContainingIgnoreCaseOrderBySkill_NameAsc(SkillType type, String skillNameQuery);

    List<UserSkill> findByTypeAndSkill_NameContainingIgnoreCaseAndUser_AvailableOrderBySkill_NameAsc(
            SkillType type, String skillNameQuery, boolean available);
}
