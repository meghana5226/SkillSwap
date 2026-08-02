package com.skillswap.dto;

public record AdminStatsResponse(
        long totalUsers,
        long totalStudents,
        long totalMentors,
        long totalAdmins,
        long totalSessions,
        long pendingSessions,
        long completedSessions,
        long totalReviews,
        double averagePlatformRating
) {
}
