package com.skillswap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Immutable record of an admin action, for accountability. Written once,
 * never updated — no setter-based mutation flow needed beyond Lombok's
 * generated ones, which simply go unused after creation.
 */
@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actor_user_id", nullable = false)
    private User actor; // the admin who performed the action

    @Column(nullable = false, length = 100)
    private String action; // e.g. "USER_DISABLED", "USER_ENABLED"

    @Column(length = 100)
    private String targetType; // e.g. "User"

    private UUID targetId;

    @Column(length = 1000)
    private String details;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
