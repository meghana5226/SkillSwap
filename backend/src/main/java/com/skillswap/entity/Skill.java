package com.skillswap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Canonical, deduplicated skill (e.g. "React", "System Design").
 * Users attach themselves to a Skill via UserSkill, either as something
 * they're OFFERING to teach or something they're LEARNING.
 */
@Entity
@Table(name = "skills", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private String category;
}
