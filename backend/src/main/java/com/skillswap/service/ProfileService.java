package com.skillswap.service;

import com.skillswap.dto.*;
import com.skillswap.entity.Skill;
import com.skillswap.entity.User;
import com.skillswap.entity.UserSkill;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.SkillRepository;
import com.skillswap.repository.UserRepository;
import com.skillswap.repository.UserSkillRepository;
import com.skillswap.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private static final Set<String> ALLOWED_RESUME_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    private static final long MAX_RESUME_BYTES = 5L * 1024 * 1024; // 5MB, matches multipart config

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final StorageService storageService;

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(String email) {
        User user = findUserByEmail(email);
        return toProfileResponse(user);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        return toProfileResponse(user);
    }

    @Transactional
    public ProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = findUserByEmail(email);

        if (request.bio() != null) user.setBio(request.bio());
        if (request.experienceLevel() != null) user.setExperienceLevel(request.experienceLevel());
        if (request.githubUrl() != null) user.setGithubUrl(request.githubUrl());
        if (request.linkedinUrl() != null) user.setLinkedinUrl(request.linkedinUrl());
        if (request.portfolioUrl() != null) user.setPortfolioUrl(request.portfolioUrl());
        if (request.location() != null) user.setLocation(request.location());
        if (request.available() != null) user.setAvailable(request.available());

        userRepository.save(user);
        return toProfileResponse(user);
    }

    @Transactional
    public String uploadResume(String email, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException("Please select a file to upload", HttpStatus.BAD_REQUEST);
        }
        if (file.getSize() > MAX_RESUME_BYTES) {
            throw new ApiException("Resume must be under 5MB", HttpStatus.BAD_REQUEST);
        }
        if (!ALLOWED_RESUME_TYPES.contains(file.getContentType())) {
            throw new ApiException("Resume must be a PDF or Word document", HttpStatus.BAD_REQUEST);
        }

        User user = findUserByEmail(email);
        String url = storageService.store(file, "resumes");
        user.setResumeUrl(url);
        userRepository.save(user);
        return url;
    }

    @Transactional
    public UserSkillResponse addSkill(String email, AddUserSkillRequest request) {
        User user = findUserByEmail(email);

        String cleanName = StringUtils.trimWhitespace(request.skillName());
        Skill skill = skillRepository.findByNameIgnoreCase(cleanName)
                .orElseGet(() -> skillRepository.save(
                        Skill.builder().name(cleanName).category(request.category()).build()));

        // Prevent duplicate "offering React" / "learning React" entries for the same user.
        userSkillRepository.findByUserIdAndSkillIdAndType(user.getId(), skill.getId(), request.type())
                .ifPresent(existing -> {
                    throw new ApiException(
                            "You've already added this skill as " + request.type().name().toLowerCase(),
                            HttpStatus.CONFLICT
                    );
                });

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .type(request.type())
                .proficiency(request.proficiency())
                .build();

        userSkillRepository.save(userSkill);
        return toUserSkillResponse(userSkill);
    }

    @Transactional
    public void removeSkill(String email, UUID userSkillId) {
        User user = findUserByEmail(email);
        UserSkill userSkill = userSkillRepository.findByIdAndUserId(userSkillId, user.getId())
                .orElseThrow(() -> new ApiException("Skill entry not found", HttpStatus.NOT_FOUND));
        userSkillRepository.delete(userSkill);
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> searchSkills(String query) {
        String safeQuery = query == null ? "" : query.trim();
        return skillRepository.findTop10ByNameContainingIgnoreCaseOrderByNameAsc(safeQuery)
                .stream()
                .map(s -> new SkillResponse(s.getId(), s.getName(), s.getCategory()))
                .toList();
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }

    private ProfileResponse toProfileResponse(User user) {
        List<UserSkillResponse> skills = userSkillRepository.findByUserId(user.getId())
                .stream()
                .map(this::toUserSkillResponse)
                .toList();

        return new ProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getBio(),
                user.getExperienceLevel(),
                user.getGithubUrl(),
                user.getLinkedinUrl(),
                user.getPortfolioUrl(),
                user.getResumeUrl(),
                user.getLocation(),
                user.isAvailable(),
                skills
        );
    }

    private UserSkillResponse toUserSkillResponse(UserSkill us) {
        return new UserSkillResponse(
                us.getId(),
                us.getSkill().getId(),
                us.getSkill().getName(),
                us.getSkill().getCategory(),
                us.getType(),
                us.getProficiency()
        );
    }
}
