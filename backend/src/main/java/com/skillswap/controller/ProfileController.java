package com.skillswap.controller;

import com.skillswap.dto.*;
import com.skillswap.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "View/update your profile, manage skills, upload resume")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    @Operation(summary = "Get the current authenticated user's profile")
    public ResponseEntity<ProfileResponse> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(profileService.getProfile(authentication.getName()));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get another user's public profile (e.g. a mentor's)")
    public ResponseEntity<ProfileResponse> getProfileById(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getProfileById(userId));
    }

    @PutMapping("/me")
    @Operation(summary = "Update the current user's profile fields")
    public ResponseEntity<ProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(authentication.getName(), request));
    }

    @PostMapping(value = "/me/resume", consumes = "multipart/form-data")
    @Operation(summary = "Upload/replace the current user's resume (PDF/DOCX, max 5MB)")
    public ResponseEntity<Map<String, String>> uploadResume(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) {
        String url = profileService.uploadResume(authentication.getName(), file);
        return ResponseEntity.ok(Map.of("resumeUrl", url));
    }

    @PostMapping("/me/skills")
    @Operation(summary = "Add a skill you're offering to teach or want to learn")
    public ResponseEntity<UserSkillResponse> addSkill(
            Authentication authentication,
            @Valid @RequestBody AddUserSkillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.addSkill(authentication.getName(), request));
    }

    @DeleteMapping("/me/skills/{userSkillId}")
    @Operation(summary = "Remove a skill from the current user's profile")
    public ResponseEntity<Void> removeSkill(Authentication authentication, @PathVariable UUID userSkillId) {
        profileService.removeSkill(authentication.getName(), userSkillId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/skills/search")
    @Operation(summary = "Search the master skill list (for autocomplete)")
    public ResponseEntity<List<SkillResponse>> searchSkills(@RequestParam(defaultValue = "") String query) {
        return ResponseEntity.ok(profileService.searchSkills(query));
    }
}
