package com.skillswap.controller;

import com.skillswap.dto.BookmarkResponse;
import com.skillswap.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@Tag(name = "Bookmarks", description = "Save mentors/learners for later")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    @Operation(summary = "List my bookmarks")
    public ResponseEntity<List<BookmarkResponse>> list(Authentication authentication) {
        return ResponseEntity.ok(bookmarkService.listBookmarks(authentication.getName()));
    }

    @PostMapping("/{userId}")
    @Operation(summary = "Bookmark a user")
    public ResponseEntity<BookmarkResponse> add(Authentication authentication, @PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookmarkService.addBookmark(authentication.getName(), userId));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Remove a bookmark")
    public ResponseEntity<Void> remove(Authentication authentication, @PathVariable UUID userId) {
        bookmarkService.removeBookmark(authentication.getName(), userId);
        return ResponseEntity.noContent().build();
    }
}
