package com.meghana.skillswap.controller;

import com.meghana.skillswap.dto.request.AddSkillRequest;
import com.meghana.skillswap.dto.response.SkillResponse;

import com.meghana.skillswap.service.SkillService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PostMapping
    public SkillResponse addSkill(
            Authentication authentication,
            @Valid @RequestBody
            AddSkillRequest request
    ) {

        return skillService.addSkill(
                authentication.getName(),
                request
        );
    }

    @GetMapping("/my-skills")
    public List<SkillResponse> getMySkills(
            Authentication authentication
    ) {

        return skillService.getMySkills(
                authentication.getName()
        );
    }

    @DeleteMapping("/{id}")
    public String deleteSkill(
            @PathVariable Long id,
            Authentication authentication
    ) {

        skillService.deleteSkill(
                id,
                authentication.getName()
        );

        return "Skill deleted successfully";
    }
}