package com.meghana.skillswap.dto.response;

import com.meghana.skillswap.entity.enums.RequestStatus;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillRequestResponse {

    private Long requestId;

    private String senderName;

    private String receiverName;

    private String skillName;

    private String message;

    private RequestStatus status;
}