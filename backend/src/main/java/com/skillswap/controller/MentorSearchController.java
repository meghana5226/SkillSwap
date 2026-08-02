package com.skillswap.controller;

import com.skillswap.dto.MentorSearchResult;
import com.skillswap.service.MentorSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mentor Search", description = "Search/filter mentors offering a given skill")
public class MentorSearchController {

    private final MentorSearchService mentorSearchService;

    @GetMapping("/api/mentors/search")
    @Operation(summary = "Search mentors by skill name, optionally filtering to only-available")
    public ResponseEntity<List<MentorSearchResult>> search(
            @RequestParam(defaultValue = "") String skill,
            @RequestParam(required = false) Boolean onlyAvailable) {
        return ResponseEntity.ok(mentorSearchService.search(skill, onlyAvailable));
    }
}
