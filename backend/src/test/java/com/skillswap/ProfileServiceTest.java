package com.skillswap;

import com.skillswap.dto.*;
import com.skillswap.entity.ProficiencyLevel;
import com.skillswap.entity.Role;
import com.skillswap.entity.SkillType;
import com.skillswap.exception.ApiException;
import com.skillswap.service.AuthService;
import com.skillswap.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProfileServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private ProfileService profileService;

    private String registerTestUser(String email) {
        authService.register(new RegisterRequest("Test User", email, "StrongPass1!", Role.STUDENT));
        return email;
    }

    @Test
    void updateProfileUpdatesOnlyProvidedFields() {
        String email = registerTestUser("profile.update@example.com");

        var updated = profileService.updateProfile(email,
                new UpdateProfileRequest("Backend enthusiast", "Intermediate", null, null, null, "Hyderabad", true));

        assertEquals("Backend enthusiast", updated.bio());
        assertEquals("Hyderabad", updated.location());
        assertTrue(updated.available());
    }

    @Test
    void addSkillCreatesSkillAndLinksToUser() {
        String email = registerTestUser("skills.add@example.com");

        var response = profileService.addSkill(email,
                new AddUserSkillRequest("React", "Frontend", SkillType.OFFERING, ProficiencyLevel.ADVANCED));

        assertEquals("React", response.skillName());
        assertEquals(SkillType.OFFERING, response.type());

        var profile = profileService.getProfile(email);
        assertEquals(1, profile.skills().size());
    }

    @Test
    void addingSameSkillTwiceWithSameTypeThrows() {
        String email = registerTestUser("skills.dup@example.com");

        profileService.addSkill(email, new AddUserSkillRequest("Java", "Backend", SkillType.OFFERING, ProficiencyLevel.EXPERT));

        assertThrows(ApiException.class, () ->
                profileService.addSkill(email, new AddUserSkillRequest("Java", "Backend", SkillType.OFFERING, ProficiencyLevel.BEGINNER)));
    }

    @Test
    void sameSkillCanBeBothOfferedAndLearnedByDifferentUsers() {
        String email = registerTestUser("skills.both@example.com");

        var offering = profileService.addSkill(email,
                new AddUserSkillRequest("Docker", "DevOps", SkillType.OFFERING, ProficiencyLevel.INTERMEDIATE));
        var learning = profileService.addSkill(email,
                new AddUserSkillRequest("Kubernetes", "DevOps", SkillType.LEARNING, null));

        assertNotEquals(offering.id(), learning.id());

        var profile = profileService.getProfile(email);
        assertEquals(2, profile.skills().size());
    }

    @Test
    void removeSkillDeletesTheEntry() {
        String email = registerTestUser("skills.remove@example.com");

        var added = profileService.addSkill(email,
                new AddUserSkillRequest("Python", "Backend", SkillType.OFFERING, ProficiencyLevel.ADVANCED));

        profileService.removeSkill(email, added.id());

        var profile = profileService.getProfile(email);
        assertEquals(0, profile.skills().size());
    }
}
