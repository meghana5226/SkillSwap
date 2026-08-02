package com.skillswap.service;

import com.skillswap.dto.MentorSearchResult;
import com.skillswap.entity.SkillType;
import com.skillswap.entity.UserSkill;
import com.skillswap.repository.ReviewRepository;
import com.skillswap.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorSearchService {

    private final UserSkillRepository userSkillRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<MentorSearchResult> search(String skillQuery, Boolean onlyAvailable) {
        String query = skillQuery == null ? "" : skillQuery.trim();

        List<UserSkill> matches = (onlyAvailable != null && onlyAvailable)
                ? userSkillRepository.findByTypeAndSkill_NameContainingIgnoreCaseAndUser_AvailableOrderBySkill_NameAsc(
                        SkillType.OFFERING, query, true)
                : userSkillRepository.findByTypeAndSkill_NameContainingIgnoreCaseOrderBySkill_NameAsc(
                        SkillType.OFFERING, query);

        return matches.stream().map(this::toResult).toList();
    }

    private MentorSearchResult toResult(UserSkill us) {
        var reviews = reviewRepository.findByMentorIdOrderByCreatedAtDesc(us.getUser().getId());
        double average = reviews.isEmpty()
                ? 0.0
                : reviews.stream().mapToInt(r -> r.getRating()).average().orElse(0.0);

        return new MentorSearchResult(
                us.getUser().getId(),
                us.getUser().getFullName(),
                us.getUser().getBio(),
                us.getUser().getExperienceLevel(),
                us.getUser().getLocation(),
                us.getUser().isAvailable(),
                us.getSkill().getId(),
                us.getSkill().getName(),
                us.getProficiency(),
                Math.round(average * 10) / 10.0,
                reviews.size()
        );
    }
}
