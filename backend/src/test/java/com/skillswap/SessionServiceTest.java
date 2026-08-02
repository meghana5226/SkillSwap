package com.skillswap;

import com.skillswap.dto.*;
import com.skillswap.entity.ProficiencyLevel;
import com.skillswap.entity.Role;
import com.skillswap.entity.SessionStatus;
import com.skillswap.entity.SkillType;
import com.skillswap.exception.ApiException;
import com.skillswap.service.AuthService;
import com.skillswap.service.ProfileService;
import com.skillswap.service.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SessionServiceTest {

    @Autowired private AuthService authService;
    @Autowired private ProfileService profileService;
    @Autowired private SessionService sessionService;

    private UUID registerMentorOfferingSkill(String email, String skillName) {
        authService.register(new RegisterRequest("Mentor " + email, email, "StrongPass1!", Role.MENTOR));
        var skill = profileService.addSkill(email,
                new AddUserSkillRequest(skillName, "Backend", SkillType.OFFERING, ProficiencyLevel.ADVANCED));
        return skill.skillId();
    }

    private String registerStudent(String email) {
        authService.register(new RegisterRequest("Student " + email, email, "StrongPass1!", Role.STUDENT));
        return email;
    }

    @Test
    void fullLifecycle_requestAcceptComplete() {
        String mentorEmail = "mentor.lifecycle@example.com";
        UUID skillId = registerMentorOfferingSkill(mentorEmail, "Kubernetes");
        String studentEmail = registerStudent("student.lifecycle@example.com");

        var mentorProfile = profileService.getProfile(mentorEmail);

        var created = sessionService.createRequest(studentEmail,
                new CreateSessionRequest(mentorProfile.id(), skillId, "Can you help me with K8s?", null));
        assertEquals(SessionStatus.PENDING, created.status());

        var accepted = sessionService.accept(mentorEmail, created.id());
        assertEquals(SessionStatus.ACCEPTED, accepted.status());

        var completed = sessionService.complete(mentorEmail, created.id());
        assertEquals(SessionStatus.COMPLETED, completed.status());
    }

    @Test
    void cannotRequestSessionWithSelf() {
        String mentorEmail = "mentor.self@example.com";
        UUID skillId = registerMentorOfferingSkill(mentorEmail, "Go");
        var mentorProfile = profileService.getProfile(mentorEmail);

        assertThrows(ApiException.class, () -> sessionService.createRequest(mentorEmail,
                new CreateSessionRequest(mentorProfile.id(), skillId, null, null)));
    }

    @Test
    void cannotRequestSkillMentorDoesNotOffer() {
        String mentorEmail = "mentor.nooffer@example.com";
        authService.register(new RegisterRequest("Mentor", mentorEmail, "StrongPass1!", Role.MENTOR));
        var mentorProfile = profileService.getProfile(mentorEmail);

        // Register an unrelated skill so we have a valid skillId that this mentor does NOT offer.
        String otherMentorEmail = "mentor.other@example.com";
        UUID unrelatedSkillId = registerMentorOfferingSkill(otherMentorEmail, "Rust");

        String studentEmail = registerStudent("student.nooffer@example.com");

        assertThrows(ApiException.class, () -> sessionService.createRequest(studentEmail,
                new CreateSessionRequest(mentorProfile.id(), unrelatedSkillId, null, null)));
    }

    @Test
    void onlyMentorCanAcceptRequest() {
        String mentorEmail = "mentor.authz@example.com";
        UUID skillId = registerMentorOfferingSkill(mentorEmail, "AWS");
        var mentorProfile = profileService.getProfile(mentorEmail);
        String studentEmail = registerStudent("student.authz@example.com");

        var created = sessionService.createRequest(studentEmail,
                new CreateSessionRequest(mentorProfile.id(), skillId, null, null));

        assertThrows(ApiException.class, () -> sessionService.accept(studentEmail, created.id()));
    }

    @Test
    void cannotAcceptAlreadyAcceptedRequest() {
        String mentorEmail = "mentor.double@example.com";
        UUID skillId = registerMentorOfferingSkill(mentorEmail, "Terraform");
        var mentorProfile = profileService.getProfile(mentorEmail);
        String studentEmail = registerStudent("student.double@example.com");

        var created = sessionService.createRequest(studentEmail,
                new CreateSessionRequest(mentorProfile.id(), skillId, null, null));
        sessionService.accept(mentorEmail, created.id());

        assertThrows(ApiException.class, () -> sessionService.accept(mentorEmail, created.id()));
    }

    @Test
    void requesterCanCancelPendingRequest() {
        String mentorEmail = "mentor.cancel@example.com";
        UUID skillId = registerMentorOfferingSkill(mentorEmail, "Redis");
        var mentorProfile = profileService.getProfile(mentorEmail);
        String studentEmail = registerStudent("student.cancel@example.com");

        var created = sessionService.createRequest(studentEmail,
                new CreateSessionRequest(mentorProfile.id(), skillId, null, null));
        var cancelled = sessionService.cancel(studentEmail, created.id());

        assertEquals(SessionStatus.CANCELLED, cancelled.status());
    }

    @Test
    void duplicatePendingRequestForSameSkillIsRejected() {
        String mentorEmail = "mentor.dupreq@example.com";
        UUID skillId = registerMentorOfferingSkill(mentorEmail, "GraphQL");
        var mentorProfile = profileService.getProfile(mentorEmail);
        String studentEmail = registerStudent("student.dupreq@example.com");

        sessionService.createRequest(studentEmail, new CreateSessionRequest(mentorProfile.id(), skillId, null, null));

        assertThrows(ApiException.class, () -> sessionService.createRequest(studentEmail,
                new CreateSessionRequest(mentorProfile.id(), skillId, null, null)));
    }
}
