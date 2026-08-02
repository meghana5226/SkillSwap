package com.skillswap.controller;

import com.skillswap.dto.DashboardStatsResponse;
import com.skillswap.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Aggregated learning/mentoring stats for the current user")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "Get my aggregated stats and 6-month session activity")
    public ResponseEntity<DashboardStatsResponse> stats(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.getStats(authentication.getName()));
    }
}
