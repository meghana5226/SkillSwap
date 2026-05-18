package com.meghana.skillswap.entity;

import com.meghana.skillswap.entity.enums.SkillLevel;
import com.meghana.skillswap.entity.enums.SkillType;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "user_skills")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "skill_id",
            nullable = false
    )
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private SkillType type;

    @Enumerated(EnumType.STRING)
    private SkillLevel level;

    @Column(length = 500)
    private String description;
}