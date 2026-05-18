package com.meghana.skillswap.service;

import com.meghana.skillswap.dto.response.SearchUserResponse;

import java.util.List;

public interface SearchService {

    List<SearchUserResponse> searchUsersBySkill(
            String skill
    );
}