package com.meghana.skillswap.entity;

import com.meghana.skillswap.entity.enums.RequestStatus;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "skill_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "sender_id",
            nullable = false
    )
    private User sender;

    @ManyToOne
    @JoinColumn(
            name = "receiver_id",
            nullable = false
    )
    private User receiver;

    @ManyToOne
    @JoinColumn(
            name = "user_skill_id",
            nullable = false
    )
    private UserSkill userSkill;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}