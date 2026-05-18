package com.meghana.skillswap.service.impl;

import com.meghana.skillswap.dto.request.SendRequestDto;
import com.meghana.skillswap.dto.response.SkillRequestResponse;

import com.meghana.skillswap.entity.SkillRequest;
import com.meghana.skillswap.entity.User;
import com.meghana.skillswap.entity.UserSkill;

import com.meghana.skillswap.entity.enums.RequestStatus;

import com.meghana.skillswap.repository.SkillRequestRepository;
import com.meghana.skillswap.repository.UserRepository;
import com.meghana.skillswap.repository.UserSkillRepository;

import com.meghana.skillswap.service.SkillRequestService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillRequestServiceImpl
        implements SkillRequestService {

    private final UserRepository userRepository;

    private final UserSkillRepository userSkillRepository;

    private final SkillRequestRepository
            skillRequestRepository;

    @Override
    public SkillRequestResponse sendRequest(
            String email,
            SendRequestDto dto
    ) {

        User sender = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        UserSkill userSkill =
                userSkillRepository
                        .findById(dto.getUserSkillId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Skill not found"
                                ));

        User receiver = userSkill.getUser();

        if (sender.getId().equals(
                receiver.getId()
        )) {

            throw new RuntimeException(
                    "You cannot request your own skill"
            );
        }

        SkillRequest request =
                SkillRequest.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .userSkill(userSkill)
                        .message(dto.getMessage())
                        .status(RequestStatus.PENDING)
                        .build();

        SkillRequest savedRequest =
                skillRequestRepository.save(request);

        return mapToResponse(savedRequest);
    }

    @Override
    public List<SkillRequestResponse>
    getReceivedRequests(String email) {

        User receiver = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        return skillRequestRepository
                .findByReceiver(receiver)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SkillRequestResponse>
    getSentRequests(String email) {

        User sender = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        return skillRequestRepository
                .findBySender(sender)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SkillRequestResponse updateRequestStatus(
            Long requestId,
            String email,
            String status
    ) {

        User receiver = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        SkillRequest request =
                skillRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Request not found"
                                ));

        if (!request.getReceiver()
                .getId()
                .equals(receiver.getId())) {

            throw new RuntimeException(
                    "Unauthorized action"
            );
        }

        request.setStatus(
                RequestStatus.valueOf(status)
        );

        SkillRequest updatedRequest =
                skillRequestRepository.save(request);

        return mapToResponse(updatedRequest);
    }

    private SkillRequestResponse mapToResponse(
            SkillRequest request
    ) {

        return SkillRequestResponse.builder()
                .requestId(request.getId())
                .senderName(
                        request.getSender().getName()
                )
                .receiverName(
                        request.getReceiver().getName()
                )
                .skillName(
                        request.getUserSkill()
                                .getSkill()
                                .getName()
                )
                .message(request.getMessage())
                .status(request.getStatus())
                .build();
    }
}