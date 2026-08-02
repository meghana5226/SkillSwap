package com.skillswap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bookmarks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "bookmarked_user_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // who bookmarked

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bookmarked_user_id", nullable = false)
    private User bookmarkedUser; // who got bookmarked

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
