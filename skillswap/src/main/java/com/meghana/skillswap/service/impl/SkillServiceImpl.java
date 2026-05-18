package com.meghana.skillswap.service.impl;

import com.meghana.skillswap.dto.request.AddSkillRequest;
import com.meghana.skillswap.dto.response.SkillResponse;

import com.meghana.skillswap.entity.Skill;
import com.meghana.skillswap.entity.User;
import com.meghana.skillswap.entity.UserSkill;

import com.meghana.skillswap.repository.SkillRepository;
import com.meghana.skillswap.repository.UserRepository;
import com.meghana.skillswap.repository.UserSkillRepository;

import com.meghana.skillswap.service.SkillService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final UserRepository userRepository;

    private final SkillRepository skillRepository;

    private final UserSkillRepository userSkillRepository;

    @Override
    public SkillResponse addSkill(
            String email,
            AddSkillRequest request
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        Skill skill = skillRepository
                .findByName(request.getSkillName())
                .orElseGet(() -> {

                    Skill newSkill = Skill.builder()
                            .name(request.getSkillName())
                            .build();

                    return skillRepository.save(newSkill);
                });

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .type(request.getType())
                .level(request.getLevel())
                .description(
                        request.getDescription()
                )
                .build();

        UserSkill savedSkill =
                userSkillRepository.save(userSkill);

        return mapToResponse(savedSkill);
    }

    @Override
    public List<SkillResponse> getMySkills(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        return userSkillRepository
                .findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteSkill(
            Long skillId,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        UserSkill skill = userSkillRepository
                .findById(skillId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Skill not found"
                        ));

        if (!skill.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Unauthorized action"
            );
        }

        userSkillRepository.delete(skill);
    }

    private SkillResponse mapToResponse(
            UserSkill userSkill
    ) {

        return SkillResponse.builder()
                .id(userSkill.getId())
                .skillName(
                        userSkill.getSkill().getName()
                )
                .type(userSkill.getType())
                .level(userSkill.getLevel())
                .description(
                        userSkill.getDescription()
                )
                .build();
    }
}