package com.skillswap.service.ai;

import com.skillswap.dto.MentorSearchResult;
import com.skillswap.dto.ProfileResponse;
import com.skillswap.dto.UserSkillResponse;
import com.skillswap.dto.ai.*;
import com.skillswap.entity.SessionStatus;
import com.skillswap.entity.SkillType;
import com.skillswap.entity.User;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.SessionRequestRepository;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.MentorSearchService;
import com.skillswap.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Each public method here is one "AI Feature" from the product spec.
 * All of them follow the same shape: gather relevant context from the
 * platform's own data (profile, skills, sessions), fold it into a prompt
 * built from PromptTemplates, and call OllamaClient. No feature calls
 * Ollama directly — they all go through this service so context-building
 * stays consistent and testable.
 */
@Service
@RequiredArgsConstructor
public class AiService {

    private final OllamaClient ollamaClient;
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final MentorSearchService mentorSearchService;
    private final SessionRequestRepository sessionRequestRepository;

    // --- 1. Learning Roadmap Generator ---
    public AiTextResponse generateRoadmap(String email, RoadmapRequest request) {
        ProfileResponse profile = profileService.getProfile(email);
        String known = summarizeSkills(profile, SkillType.OFFERING);

        String prompt = """
                Learner's known/offered skills: %s
                Target skill to learn: %s
                Current level with this target skill: %s

                Build a phased learning roadmap to go from where they are now to
                a solid working level in the target skill.
                """.formatted(
                known.isBlank() ? "none listed yet" : known,
                request.targetSkill(),
                request.currentLevel() == null || request.currentLevel().isBlank() ? "not specified" : request.currentLevel()
        );

        return new AiTextResponse(ollamaClient.generate(PromptTemplates.ROADMAP_SYSTEM, prompt));
    }

    // --- 2. Skill Gap Analysis ---
    public AiTextResponse analyzeSkillGap(String email, SkillGapRequest request) {
        ProfileResponse profile = profileService.getProfile(email);
        String known = summarizeSkills(profile, SkillType.OFFERING);

        String prompt = """
                Learner's current skills: %s
                Target role: %s

                Identify the gap between their current skills and what this role
                typically requires.
                """.formatted(known.isBlank() ? "none listed yet" : known, request.targetRole());

        return new AiTextResponse(ollamaClient.generate(PromptTemplates.SKILL_GAP_SYSTEM, prompt));
    }

    // --- 3. Project Suggestions ---
    public AiTextResponse suggestProjects(ProjectIdeasRequest request) {
        String prompt = """
                Skill to practice: %s
                Learner's level: %s
                """.formatted(request.skill(), request.level() == null || request.level().isBlank() ? "not specified" : request.level());

        return new AiTextResponse(ollamaClient.generate(PromptTemplates.PROJECT_IDEAS_SYSTEM, prompt));
    }

    // --- 4. Resume Review ---
    // Note: takes pasted resume text rather than parsing the uploaded PDF/DOCX
    // directly — keeps this feature independent of a PDF-parsing dependency.
    // Wiring it to auto-extract text from the uploaded resume is a natural
    // follow-up (see README roadmap).
    public AiTextResponse reviewResume(ResumeReviewRequest request) {
        String prompt = "Resume text:\n" + request.resumeText();
        return new AiTextResponse(ollamaClient.generate(PromptTemplates.RESUME_REVIEW_SYSTEM, prompt));
    }

    // --- 5. Tech Interview Tips ---
    public AiTextResponse interviewTips(InterviewTipsRequest request) {
        String prompt = "Topic/skill: " + request.skill();
        return new AiTextResponse(ollamaClient.generate(PromptTemplates.INTERVIEW_TIPS_SYSTEM, prompt));
    }

    // --- 6. Weekly Study Planner ---
    public AiTextResponse weeklyStudyPlan(StudyPlanRequest request) {
        String prompt = "Skill: %s\nHours available this week: %d".formatted(request.skill(), request.hoursPerWeek());
        return new AiTextResponse(ollamaClient.generate(PromptTemplates.STUDY_PLAN_SYSTEM, prompt));
    }

    // --- 7 & 8. Mentor Recommendation + Smart Skill Matching ---
    // These two features are the same underlying mechanism: match a
    // learner's desired skill against the mentor pool. The DB query does
    // the matching/ranking (see MentorSearchService); the AI layer adds a
    // short, readable explanation on top of the *actual* candidates found —
    // it never invents mentors that aren't in the search results.
    @Transactional(readOnly = true)
    public MentorRecommendationResponse recommendMentor(String email, MentorRecommendationRequest request) {
        ProfileResponse profile = profileService.getProfile(email);

        String targetSkill = (request.skill() != null && !request.skill().isBlank())
                ? request.skill()
                : profile.skills().stream()
                        .filter(s -> s.type() == SkillType.LEARNING)
                        .map(UserSkillResponse::skillName)
                        .findFirst()
                        .orElseThrow(() -> new ApiException(
                                "Add a skill you want to learn to your profile first, or specify one directly.",
                                HttpStatus.BAD_REQUEST));

        List<MentorSearchResult> candidates = mentorSearchService.search(targetSkill, null).stream()
                .filter(m -> !m.userId().equals(profile.id())) // never recommend yourself
                .limit(8)
                .toList();

        if (candidates.isEmpty()) {
            return new MentorRecommendationResponse(
                    "No mentors currently offer **" + targetSkill + "**. Check back later, or broaden your search.",
                    List.of()
            );
        }

        String candidateList = candidates.stream()
                .map(c -> "- %s | skill: %s (%s) | rating: %.1f (%d reviews) | available: %s | bio: %s".formatted(
                        c.fullName(), c.skillName(),
                        c.proficiency() == null ? "n/a" : c.proficiency(),
                        c.averageRating(), c.reviewCount(),
                        c.available() ? "yes" : "no",
                        c.bio() == null ? "—" : c.bio()))
                .collect(Collectors.joining("\n"));

        String prompt = "Learner's goal: learn " + targetSkill + "\n\nCandidate mentors:\n" + candidateList;
        String recommendation = ollamaClient.generate(PromptTemplates.MENTOR_RECOMMENDATION_SYSTEM, prompt);

        return new MentorRecommendationResponse(recommendation, candidates);
    }

    // --- 9. Personalized Dashboard Summary ---
    @Transactional(readOnly = true)
    public AiTextResponse dashboardSummary(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        ProfileResponse profile = profileService.getProfile(email);

        long completedAsLearner = sessionRequestRepository.findByRequesterIdOrderByCreatedAtDesc(user.getId())
                .stream().filter(s -> s.getStatus() == SessionStatus.COMPLETED).count();
        long completedAsMentor = sessionRequestRepository.findByMentorIdOrderByCreatedAtDesc(user.getId())
                .stream().filter(s -> s.getStatus() == SessionStatus.COMPLETED).count();
        long pendingIncoming = sessionRequestRepository.findByMentorIdOrderByCreatedAtDesc(user.getId())
                .stream().filter(s -> s.getStatus() == SessionStatus.PENDING).count();

        String prompt = """
                Name: %s
                Role: %s
                Offering: %s
                Learning: %s
                Completed sessions as learner: %d
                Completed sessions as mentor: %d
                Pending incoming requests to respond to: %d
                """.formatted(
                profile.fullName(), profile.role(),
                summarizeSkills(profile, SkillType.OFFERING),
                summarizeSkills(profile, SkillType.LEARNING),
                completedAsLearner, completedAsMentor, pendingIncoming
        );

        return new AiTextResponse(ollamaClient.generate(PromptTemplates.DASHBOARD_SUMMARY_SYSTEM, prompt));
    }

    // --- 10. Chat Assistant ---
    public AiTextResponse chat(ChatRequest request) {
        StringBuilder conversation = new StringBuilder();
        if (request.history() != null) {
            for (var turn : request.history()) {
                conversation.append(turn.role()).append(": ").append(turn.content()).append("\n");
            }
        }
        conversation.append("user: ").append(request.message());

        return new AiTextResponse(ollamaClient.generate(PromptTemplates.CHAT_ASSISTANT_SYSTEM, conversation.toString()));
    }

    private String summarizeSkills(ProfileResponse profile, SkillType type) {
        return profile.skills().stream()
                .filter(s -> s.type() == type)
                .map(s -> s.proficiency() != null ? s.skillName() + " (" + s.proficiency() + ")" : s.skillName())
                .collect(Collectors.joining(", "));
    }
}
