package com.skillswap.service;

import com.skillswap.dto.BookmarkResponse;
import com.skillswap.entity.Bookmark;
import com.skillswap.entity.User;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.BookmarkRepository;
import com.skillswap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookmarkResponse addBookmark(String userEmail, UUID targetUserId) {
        User user = findUser(userEmail);
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        if (user.getId().equals(target.getId())) {
            throw new ApiException("You can't bookmark yourself", HttpStatus.BAD_REQUEST);
        }
        if (bookmarkRepository.existsByUserIdAndBookmarkedUserId(user.getId(), target.getId())) {
            throw new ApiException("Already bookmarked", HttpStatus.CONFLICT);
        }

        Bookmark bookmark = Bookmark.builder().user(user).bookmarkedUser(target).build();
        bookmarkRepository.save(bookmark);
        return toResponse(bookmark);
    }

    @Transactional
    public void removeBookmark(String userEmail, UUID targetUserId) {
        User user = findUser(userEmail);
        Bookmark bookmark = bookmarkRepository.findByUserIdAndBookmarkedUserId(user.getId(), targetUserId)
                .orElseThrow(() -> new ApiException("Bookmark not found", HttpStatus.NOT_FOUND));
        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponse> listBookmarks(String userEmail) {
        User user = findUser(userEmail);
        return bookmarkRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::toResponse).toList();
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }

    private BookmarkResponse toResponse(Bookmark b) {
        return new BookmarkResponse(
                b.getId(),
                b.getBookmarkedUser().getId(),
                b.getBookmarkedUser().getFullName(),
                b.getBookmarkedUser().getRole().name(),
                b.getCreatedAt()
        );
    }
}
