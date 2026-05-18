package com.meghana.skillswap.repository;

import com.meghana.skillswap.entity.User;
import com.meghana.skillswap.entity.UserSkill;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkillRepository
        extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUser(User user);

    List<UserSkill> findBySkill_NameContainingIgnoreCase(
            String skillName
    );

    long countByUser_Email(String email);

    long countByUser_EmailAndType(
            String email,
            com.meghana.skillswap.entity.enums.SkillType type
    );
}