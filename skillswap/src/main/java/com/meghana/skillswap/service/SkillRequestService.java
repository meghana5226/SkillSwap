package com.meghana.skillswap.service;

import com.meghana.skillswap.dto.request.SendRequestDto;
import com.meghana.skillswap.dto.response.SkillRequestResponse;

import java.util.List;

public interface SkillRequestService {

    SkillRequestResponse sendRequest(
            String email,
            SendRequestDto dto
    );

    List<SkillRequestResponse>
    getReceivedRequests(String email);

    List<SkillRequestResponse>
    getSentRequests(String email);

    SkillRequestResponse updateRequestStatus(
            Long requestId,
            String email,
            String status
    );
}