package com.skillswap.service;

import com.skillswap.dto.DashboardStatsResponse;
import com.skillswap.dto.MonthlyActivity;
import com.skillswap.dto.ProfileResponse;
import com.skillswap.entity.SessionRequest;
import com.skillswap.entity.SessionStatus;
import com.skillswap.entity.SkillType;
import com.skillswap.entity.User;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.ReviewRepository;
import com.skillswap.repository.SessionRequestRepository;
import com.skillswap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final int MONTHS_OF_HISTORY = 6;

    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final SessionRequestRepository sessionRequestRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        ProfileResponse profile = profileService.getProfile(email);
        int skillsOffering = (int) profile.skills().stream().filter(s -> s.type() == SkillType.OFFERING).count();
        int skillsLearning = (int) profile.skills().stream().filter(s -> s.type() == SkillType.LEARNING).count();

        List<SessionRequest> asLearner = sessionRequestRepository.findByRequesterIdOrderByCreatedAtDesc(user.getId());
        List<SessionRequest> asMentor = sessionRequestRepository.findByMentorIdOrderByCreatedAtDesc(user.getId());

        long completedAsLearner = asLearner.stream().filter(s -> s.getStatus() == SessionStatus.COMPLETED).count();
        long completedAsMentor = asMentor.stream().filter(s -> s.getStatus() == SessionStatus.COMPLETED).count();
        long pendingOutgoing = asLearner.stream().filter(s -> s.getStatus() == SessionStatus.PENDING).count();
        long pendingIncoming = asMentor.stream().filter(s -> s.getStatus() == SessionStatus.PENDING).count();

        var reviews = reviewRepository.findByMentorIdOrderByCreatedAtDesc(user.getId());
        double averageRating = reviews.isEmpty()
                ? 0.0
                : Math.round(reviews.stream().mapToInt(r -> r.getRating()).average().orElse(0.0) * 10) / 10.0;

        List<MonthlyActivity> sessionActivity = buildMonthlyActivity(
                Stream.concat(asLearner.stream(), asMentor.stream()).toList()
        );

        return new DashboardStatsResponse(
                skillsOffering, skillsLearning,
                completedAsLearner, completedAsMentor,
                pendingIncoming, pendingOutgoing,
                averageRating, reviews.size(),
                sessionActivity
        );
    }

    private List<MonthlyActivity> buildMonthlyActivity(List<SessionRequest> allSessions) {
        Map<YearMonth, Long> completedByMonth = allSessions.stream()
                .filter(s -> s.getStatus() == SessionStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                        s -> YearMonth.from(s.getUpdatedAt().atZone(ZoneOffset.UTC)),
                        Collectors.counting()
                ));

        YearMonth current = YearMonth.now(ZoneOffset.UTC);
        List<MonthlyActivity> result = new ArrayList<>();
        for (int i = MONTHS_OF_HISTORY - 1; i >= 0; i--) {
            YearMonth month = current.minusMonths(i);
            String label = month.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + month.getYear();
            result.add(new MonthlyActivity(label, completedByMonth.getOrDefault(month, 0L)));
        }
        return result;
    }
}
