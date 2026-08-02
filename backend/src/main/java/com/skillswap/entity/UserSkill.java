package com.skillswap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_skills", uniqueConstraints = {
        // A user can't add the same skill twice under the same type
        // (e.g. can't "offer React" twice), but CAN both offer and learn
        // different skills, or even offer one skill while learning another.
        @UniqueConstraint(columnNames = {"user_id", "skill_id", "type"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSkill {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillType type;

    @Enumerated(EnumType.STRING)
    private ProficiencyLevel proficiency; // only meaningful when type == OFFERING
}
