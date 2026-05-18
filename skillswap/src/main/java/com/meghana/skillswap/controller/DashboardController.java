package com.meghana.skillswap.controller;

import com.meghana.skillswap.dto.response.DashboardStatsResponse;

import com.meghana.skillswap.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService
            dashboardService;

    @GetMapping("/stats")
    public DashboardStatsResponse
    getDashboardStats(
            Authentication authentication
    ) {

        return dashboardService.getDashboardStats(
                authentication.getName()
        );
    }
}