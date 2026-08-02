package com.skillswap.repository;

import com.skillswap.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {

    List<Bookmark> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<Bookmark> findByUserIdAndBookmarkedUserId(UUID userId, UUID bookmarkedUserId);

    boolean existsByUserIdAndBookmarkedUserId(UUID userId, UUID bookmarkedUserId);
}
