package com.meghana.skillswap.controller;

import com.meghana.skillswap.dto.response.SearchUserResponse;

import com.meghana.skillswap.service.SearchService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/users")
    public List<SearchUserResponse>
    searchUsersBySkill(
            @RequestParam String skill
    ) {

        return searchService
                .searchUsersBySkill(skill);
    }
}