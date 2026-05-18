package com.meghana.skillswap.service.impl;

import com.meghana.skillswap.dto.response.SearchUserResponse;

import com.meghana.skillswap.entity.UserSkill;

import com.meghana.skillswap.repository.UserSkillRepository;

import com.meghana.skillswap.service.SearchService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl
        implements SearchService {

    private final UserSkillRepository
            userSkillRepository;

    @Override
    public List<SearchUserResponse>
    searchUsersBySkill(String skill) {

        List<UserSkill> userSkills =
                userSkillRepository
                        .findBySkill_NameContainingIgnoreCase(
                                skill
                        );

        return userSkills.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private SearchUserResponse mapToResponse(
            UserSkill userSkill
    ) {

        return SearchUserResponse.builder()
                .userId(
                        userSkill.getUser().getId()
                )
                .name(
                        userSkill.getUser().getName()
                )
                .headline(
                        userSkill.getUser().getHeadline()
                )
                .location(
                        userSkill.getUser().getLocation()
                )
                .rating(
                        userSkill.getUser().getRating()
                )
                .profilePicture(
                        userSkill.getUser()
                                .getProfilePicture()
                )
                .skillName(
                        userSkill.getSkill().getName()
                )
                .level(userSkill.getLevel())
                .type(userSkill.getType())
                .build();
    }
}