package com.skillswap.dto;

import java.util.List;

public record DashboardStatsResponse(
        int skillsOffering,
        int skillsLearning,
        long completedAsLearner,
        long completedAsMentor,
        long pendingIncoming,
        long pendingOutgoing,
        double averageRatingReceived,
        long reviewCount,
        List<MonthlyActivity> sessionActivity
) {
}
