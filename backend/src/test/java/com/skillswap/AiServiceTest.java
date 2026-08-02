package com.skillswap;

import com.skillswap.dto.*;
import com.skillswap.dto.ai.*;
import com.skillswap.entity.ProficiencyLevel;
import com.skillswap.entity.Role;
import com.skillswap.entity.SkillType;
import com.skillswap.exception.ApiException;
import com.skillswap.service.AuthService;
import com.skillswap.service.ProfileService;
import com.skillswap.service.ai.AiService;
import com.skillswap.service.ai.OllamaClient;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * OllamaClient is mocked here — these tests verify that AiService builds
 * correct, context-rich prompts and wires responses through correctly,
 * without requiring a real Ollama server to be running.
 */
@SpringBootTest
@Transactional
class AiServiceTest {

    @Autowired private AuthService authService;
    @Autowired private ProfileService profileService;
    @Autowired private AiService aiService;

    @MockBean
    private OllamaClient ollamaClient;

    @Test
    void roadmapIncludesUsersKnownSkillsInPrompt() {
        String email = "roadmap.user@example.com";
        authService.register(new RegisterRequest("Roadmap User", email, "StrongPass1!", Role.STUDENT));
        profileService.addSkill(email, new AddUserSkillRequest("HTML", "Frontend", SkillType.OFFERING, ProficiencyLevel.BEGINNER));

        when(ollamaClient.generate(anyString(), anyString())).thenReturn("## Your roadmap\n1. Learn X");

        var response = aiService.generateRoadmap(email, new RoadmapRequest("React", "know basics"));

        ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
        verify(ollamaClient).generate(anyString(), promptCaptor.capture());

        assertTrue(promptCaptor.getValue().contains("HTML"));
        assertTrue(promptCaptor.getValue().contains("React"));
        assertEquals("## Your roadmap\n1. Learn X", response.content());
    }

    @Test
    void mentorRecommendationUsesLearningSkillWhenNoneSpecified() {
        String mentorEmail = "mentor.ai@example.com";
        authService.register(new RegisterRequest("AI Mentor", mentorEmail, "StrongPass1!", Role.MENTOR));
        profileService.addSkill(mentorEmail, new AddUserSkillRequest("Django", "Backend", SkillType.OFFERING, ProficiencyLevel.EXPERT));

        String studentEmail = "student.ai@example.com";
        authService.register(new RegisterRequest("AI Student", studentEmail, "StrongPass1!", Role.STUDENT));
        profileService.addSkill(studentEmail, new AddUserSkillRequest("Django", "Backend", SkillType.LEARNING, null));

        when(ollamaClient.generate(anyString(), anyString())).thenReturn("Go with AI Mentor — strong Django expert.");

        var response = aiService.recommendMentor(studentEmail, new MentorRecommendationRequest(null));

        assertFalse(response.candidates().isEmpty());
        assertEquals("AI Mentor", response.candidates().get(0).fullName());
        assertEquals("Go with AI Mentor — strong Django expert.", response.recommendation());
    }

    @Test
    void mentorRecommendationThrowsWhenNoSkillSpecifiedAndNoneLearning() {
        String studentEmail = "student.noskill@example.com";
        authService.register(new RegisterRequest("No Skill", studentEmail, "StrongPass1!", Role.STUDENT));

        assertThrows(ApiException.class, () -> aiService.recommendMentor(studentEmail, new MentorRecommendationRequest(null)));
    }

    @Test
    void mentorRecommendationNeverRecommendsSelf() {
        String email = "self.mentor@example.com";
        authService.register(new RegisterRequest("Self Mentor", email, "StrongPass1!", Role.MENTOR));
        profileService.addSkill(email, new AddUserSkillRequest("Vue", "Frontend", SkillType.OFFERING, ProficiencyLevel.ADVANCED));

        when(ollamaClient.generate(anyString(), anyString())).thenReturn("No other mentors found.");

        var response = aiService.recommendMentor(email, new MentorRecommendationRequest("Vue"));

        assertTrue(response.candidates().stream().noneMatch(c -> c.fullName().equals("Self Mentor")));
    }

    @Test
    void chatPassesMessageThroughToOllama() {
        when(ollamaClient.generate(anyString(), anyString())).thenReturn("Sure, happy to help!");

        var response = aiService.chat(new ChatRequest("How do I start with Docker?", null));

        assertEquals("Sure, happy to help!", response.content());
    }
}
