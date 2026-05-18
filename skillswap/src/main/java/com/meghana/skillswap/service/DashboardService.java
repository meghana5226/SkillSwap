package com.meghana.skillswap.service;

import com.meghana.skillswap.dto.response.DashboardStatsResponse;

public interface DashboardService {

    DashboardStatsResponse getDashboardStats(
            String email
    );
}