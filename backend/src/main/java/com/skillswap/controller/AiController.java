package com.skillswap.controller;

import com.skillswap.dto.ai.*;
import com.skillswap.service.ai.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Features", description = "Free, local-model-powered AI features (via Ollama) — roadmap, skill gap, resume review, chat, and more")
public class AiController {

    private final AiService aiService;

    @PostMapping("/roadmap")
    @Operation(summary = "Generate a personalized learning roadmap toward a target skill")
    public ResponseEntity<AiTextResponse> roadmap(Authentication authentication, @Valid @RequestBody RoadmapRequest request) {
        return ResponseEntity.ok(aiService.generateRoadmap(authentication.getName(), request));
    }

    @PostMapping("/skill-gap")
    @Operation(summary = "Analyze the gap between current skills and a target role")
    public ResponseEntity<AiTextResponse> skillGap(Authentication authentication, @Valid @RequestBody SkillGapRequest request) {
        return ResponseEntity.ok(aiService.analyzeSkillGap(authentication.getName(), request));
    }

    @PostMapping("/project-ideas")
    @Operation(summary = "Suggest hands-on projects for a skill and level")
    public ResponseEntity<AiTextResponse> projectIdeas(@Valid @RequestBody ProjectIdeasRequest request) {
        return ResponseEntity.ok(aiService.suggestProjects(request));
    }

    @PostMapping("/resume-review")
    @Operation(summary = "Get feedback on pasted resume text")
    public ResponseEntity<AiTextResponse> resumeReview(@Valid @RequestBody ResumeReviewRequest request) {
        return ResponseEntity.ok(aiService.reviewResume(request));
    }

    @PostMapping("/interview-tips")
    @Operation(summary = "Get technical interview tips for a skill/topic")
    public ResponseEntity<AiTextResponse> interviewTips(@Valid @RequestBody InterviewTipsRequest request) {
        return ResponseEntity.ok(aiService.interviewTips(request));
    }

    @PostMapping("/study-plan")
    @Operation(summary = "Generate a one-week study plan for a skill given available hours")
    public ResponseEntity<AiTextResponse> studyPlan(@Valid @RequestBody StudyPlanRequest request) {
        return ResponseEntity.ok(aiService.weeklyStudyPlan(request));
    }

    @PostMapping("/mentor-recommendation")
    @Operation(summary = "Recommend the best-fit mentor(s) for a skill (smart skill matching + AI reasoning)")
    public ResponseEntity<MentorRecommendationResponse> mentorRecommendation(
            Authentication authentication, @Valid @RequestBody MentorRecommendationRequest request) {
        return ResponseEntity.ok(aiService.recommendMentor(authentication.getName(), request));
    }

    @GetMapping("/dashboard-summary")
    @Operation(summary = "Get a short personalized summary of the current user's activity")
    public ResponseEntity<AiTextResponse> dashboardSummary(Authentication authentication) {
        return ResponseEntity.ok(aiService.dashboardSummary(authentication.getName()));
    }

    @PostMapping("/chat")
    @Operation(summary = "Chat with the SkillSwap AI assistant")
    public ResponseEntity<AiTextResponse> chat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(aiService.chat(request));
    }
}
