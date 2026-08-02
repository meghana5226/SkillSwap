package com.skillswap;

import com.skillswap.dto.*;
import com.skillswap.entity.ProficiencyLevel;
import com.skillswap.entity.Role;
import com.skillswap.entity.SkillType;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AdminAndDashboardTest {

    @Autowired private AuthService authService;
    @Autowired private ProfileService profileService;
    @Autowired private SessionService sessionService;
    @Autowired private AdminService adminService;
    @Autowired private AuditLogService auditLogService;
    @Autowired private DashboardService dashboardService;
    @Autowired private NotificationService notificationService;
    @Autowired private UserRepository userRepository;

    @Test
    void adminCanDisableAUserAndItIsAudited() {
        String adminEmail = "admin.test@example.com";
        authService.register(new RegisterRequest("Admin", adminEmail, "StrongPass1!", Role.ADMIN));

        String targetEmail = "target.disable@example.com";
        authService.register(new RegisterRequest("Target User", targetEmail, "StrongPass1!", Role.STUDENT));
        var target = profileService.getProfile(targetEmail);

        var updated = adminService.setUserEnabled(adminEmail, target.id(), false);
        assertFalse(updated.enabled());

        var logs = auditLogService.recent();
        assertTrue(logs.stream().anyMatch(l -> l.action().equals("USER_DISABLED") && l.targetId().equals(target.id())));
    }

    @Test
    void adminCannotDisableSelf() {
        String adminEmail = "admin.self@example.com";
        authService.register(new RegisterRequest("Admin Self", adminEmail, "StrongPass1!", Role.ADMIN));
        var admin = profileService.getProfile(adminEmail);

        assertThrows(ApiException.class, () -> adminService.setUserEnabled(adminEmail, admin.id(), false));
    }

    @Test
    void adminStatsCountUsersByRole() {
        authService.register(new RegisterRequest("Stat Student", "stat.student@example.com", "StrongPass1!", Role.STUDENT));
        authService.register(new RegisterRequest("Stat Mentor", "stat.mentor@example.com", "StrongPass1!", Role.MENTOR));

        var stats = adminService.getStats();
        assertTrue(stats.totalUsers() >= 2);
        assertTrue(stats.totalStudents() >= 1);
        assertTrue(stats.totalMentors() >= 1);
    }

    @Test
    void dashboardStatsReflectSkillsAndPendingSessions() {
        String mentorEmail = "mentor.dashboard@example.com";
        authService.register(new RegisterRequest("Dash Mentor", mentorEmail, "StrongPass1!", Role.MENTOR));
        profileService.addSkill(mentorEmail, new AddUserSkillRequest("Spring Boot", "Backend", SkillType.OFFERING, ProficiencyLevel.EXPERT));
        var mentorProfile = profileService.getProfile(mentorEmail);

        String studentEmail = "student.dashboard@example.com";
        authService.register(new RegisterRequest("Dash Student", studentEmail, "StrongPass1!", Role.STUDENT));
        var skill = profileService.addSkill(studentEmail,
                new AddUserSkillRequest("Spring Boot", "Backend", SkillType.LEARNING, null));

        sessionService.createRequest(studentEmail,
                new CreateSessionRequest(mentorProfile.id(), skill.skillId(), null, null));

        var studentStats = dashboardService.getStats(studentEmail);
        assertEquals(1, studentStats.skillsLearning());
        assertEquals(1, studentStats.pendingOutgoing());

        var mentorStats = dashboardService.getStats(mentorEmail);
        assertEquals(1, mentorStats.skillsOffering());
        assertEquals(1, mentorStats.pendingIncoming());
        assertEquals(6, mentorStats.sessionActivity().size());
    }

    @Test
    void notificationCreatedOnSessionRequestIsReadableByMentor() {
        String mentorEmail = "mentor.notif@example.com";
        authService.register(new RegisterRequest("Notif Mentor", mentorEmail, "StrongPass1!", Role.MENTOR));
        var skill = profileService.addSkill(mentorEmail,
                new AddUserSkillRequest("Rust", "Backend", SkillType.OFFERING, ProficiencyLevel.ADVANCED));
        var mentorProfile = profileService.getProfile(mentorEmail);

        String studentEmail = "student.notif@example.com";
        authService.register(new RegisterRequest("Notif Student", studentEmail, "StrongPass1!", Role.STUDENT));

        sessionService.createRequest(studentEmail,
                new CreateSessionRequest(mentorProfile.id(), skill.skillId(), "Help please", null));

        var mentorUser = userRepository.findByEmail(mentorEmail).orElseThrow();
        var notifications = notificationService.listMine(mentorUser.getId());

        assertFalse(notifications.isEmpty());
        assertEquals(1, notificationService.unreadCount(mentorUser.getId()));
    }
}
