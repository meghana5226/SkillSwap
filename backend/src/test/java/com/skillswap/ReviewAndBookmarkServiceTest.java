package com.skillswap;

import com.skillswap.dto.*;
import com.skillswap.entity.ProficiencyLevel;
import com.skillswap.entity.Role;
import com.skillswap.entity.SkillType;
import com.skillswap.exception.ApiException;
import com.skillswap.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewAndBookmarkServiceTest {

    @Autowired private AuthService authService;
    @Autowired private ProfileService profileService;
    @Autowired private SessionService sessionService;
    @Autowired private ReviewService reviewService;
    @Autowired private BookmarkService bookmarkService;

    @Test
    void canReviewOnlyAfterSessionCompleted() {
        String mentorEmail = "mentor.review@example.com";
        authService.register(new RegisterRequest("Mentor", mentorEmail, "StrongPass1!", Role.MENTOR));
        var skill = profileService.addSkill(mentorEmail,
                new AddUserSkillRequest("Vue", "Frontend", SkillType.OFFERING, ProficiencyLevel.ADVANCED));
        var mentorProfile = profileService.getProfile(mentorEmail);

        String studentEmail = "student.review@example.com";
        authService.register(new RegisterRequest("Student", studentEmail, "StrongPass1!", Role.STUDENT));

        var created = sessionService.createRequest(studentEmail,
                new CreateSessionRequest(mentorProfile.id(), skill.skillId(), null, null));

        // Not yet completed -> reviewing should fail.
        assertThrows(ApiException.class, () ->
                reviewService.submitReview(studentEmail, created.id(), new ReviewRequest(5, "Great!")));

        sessionService.accept(mentorEmail, created.id());
        sessionService.complete(mentorEmail, created.id());

        var review = reviewService.submitReview(studentEmail, created.id(), new ReviewRequest(5, "Excellent mentor"));
        assertEquals(5, review.rating());

        // Second review on same session should fail.
        assertThrows(ApiException.class, () ->
                reviewService.submitReview(studentEmail, created.id(), new ReviewRequest(4, "Again")));

        var mentorReviews = reviewService.getReviewsForMentor(mentorProfile.id());
        assertEquals(1, mentorReviews.size());
    }

    @Test
    void bookmarkAddRemoveAndPreventSelfBookmark() {
        String userAEmail = "usera.bookmark@example.com";
        authService.register(new RegisterRequest("User A", userAEmail, "StrongPass1!", Role.STUDENT));
        var userA = profileService.getProfile(userAEmail);

        String userBEmail = "userb.bookmark@example.com";
        authService.register(new RegisterRequest("User B", userBEmail, "StrongPass1!", Role.MENTOR));
        var userB = profileService.getProfile(userBEmail);

        assertThrows(ApiException.class, () -> bookmarkService.addBookmark(userAEmail, userA.id()));

        var bookmark = bookmarkService.addBookmark(userAEmail, userB.id());
        assertEquals(userB.id(), bookmark.bookmarkedUserId());

        assertThrows(ApiException.class, () -> bookmarkService.addBookmark(userAEmail, userB.id()));

        assertEquals(1, bookmarkService.listBookmarks(userAEmail).size());

        bookmarkService.removeBookmark(userAEmail, userB.id());
        assertEquals(0, bookmarkService.listBookmarks(userAEmail).size());
    }
}
