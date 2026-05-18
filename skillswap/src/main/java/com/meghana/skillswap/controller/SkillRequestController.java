package com.meghana.skillswap.controller;

import com.meghana.skillswap.dto.request.SendRequestDto;
import com.meghana.skillswap.dto.response.SkillRequestResponse;

import com.meghana.skillswap.service.SkillRequestService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class SkillRequestController {

    private final SkillRequestService
            skillRequestService;

    @PostMapping
    public SkillRequestResponse sendRequest(
            Authentication authentication,
            @Valid @RequestBody
            SendRequestDto dto
    ) {

        return skillRequestService.sendRequest(
                authentication.getName(),
                dto
        );
    }

    @GetMapping("/received")
    public List<SkillRequestResponse>
    getReceivedRequests(
            Authentication authentication
    ) {

        return skillRequestService
                .getReceivedRequests(
                        authentication.getName()
                );
    }

    @GetMapping("/sent")
    public List<SkillRequestResponse>
    getSentRequests(
            Authentication authentication
    ) {

        return skillRequestService
                .getSentRequests(
                        authentication.getName()
                );
    }

    @PutMapping("/{id}/status")
    public SkillRequestResponse
    updateRequestStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication
    ) {

        return skillRequestService
                .updateRequestStatus(
                        id,
                        authentication.getName(),
                        status
                );
    }
}