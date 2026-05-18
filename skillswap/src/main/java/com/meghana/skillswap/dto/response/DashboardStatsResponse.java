package com.meghana.skillswap.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    private long totalSkills;

    private long offeredSkills;

    private long wantedSkills;

    private long sentRequests;

    private long receivedRequests;

    private long acceptedRequests;

    private long completedRequests;
}