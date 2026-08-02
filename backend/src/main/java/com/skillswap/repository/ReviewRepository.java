package com.skillswap.repository;

import com.skillswap.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByMentorIdOrderByCreatedAtDesc(UUID mentorId);

    Optional<Review> findBySessionId(UUID sessionId);

    boolean existsBySessionId(UUID sessionId);

    @Query("select avg(r.rating) from Review r")
    Double averageRating();
}
