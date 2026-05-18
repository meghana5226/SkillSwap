package com.meghana.skillswap.service.impl;

import com.meghana.skillswap.dto.response.DashboardStatsResponse;

import com.meghana.skillswap.entity.enums.RequestStatus;
import com.meghana.skillswap.entity.enums.SkillType;

import com.meghana.skillswap.repository.SkillRequestRepository;
import com.meghana.skillswap.repository.UserSkillRepository;

import com.meghana.skillswap.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl
        implements DashboardService {

    private final UserSkillRepository
            userSkillRepository;

    private final SkillRequestRepository
            skillRequestRepository;

    @Override
    public DashboardStatsResponse
    getDashboardStats(String email) {

        long totalSkills =
                userSkillRepository
                        .countByUser_Email(email);

        long offeredSkills =
                userSkillRepository
                        .countByUser_EmailAndType(
                                email,
                                SkillType.OFFERED
                        );

        long wantedSkills =
                userSkillRepository
                        .countByUser_EmailAndType(
                                email,
                                SkillType.WANTED
                        );

        long sentRequests =
                skillRequestRepository
                        .countBySender_Email(email);

        long receivedRequests =
                skillRequestRepository
                        .countByReceiver_Email(email);

        long acceptedRequests =
                skillRequestRepository
                        .countByReceiver_EmailAndStatus(
                                email,
                                RequestStatus.ACCEPTED
                        );

        long completedRequests =
                skillRequestRepository
                        .countByReceiver_EmailAndStatus(
                                email,
                                RequestStatus.COMPLETED
                        );

        return DashboardStatsResponse
                .builder()
                .totalSkills(totalSkills)
                .offeredSkills(offeredSkills)
                .wantedSkills(wantedSkills)
                .sentRequests(sentRequests)
                .receivedRequests(receivedRequests)
                .acceptedRequests(acceptedRequests)
                .completedRequests(completedRequests)
                .build();
    }
}