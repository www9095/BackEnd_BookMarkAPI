package com.bookmarker.api.controller;

import com.bookmarker.api.domain.Bookmark;
import com.bookmarker.api.dto.BookmarkDTO;
import com.bookmarker.api.dto.BookmarksDTO;
import com.bookmarker.api.dto.CreateBookmarkRequest;
import com.bookmarker.api.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    //api/bookmarks?page=2
    @GetMapping
    public BookmarksDTO getBookmarks(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "query", defaultValue = "") String query) {
        /**
         * java 11에서 추가된 메소드
         * isBlank => trim() + isEmpty()
         */
        if(query == null || query.isBlank()) {
            return bookmarkService.getBookmarks(page);
        }
        return bookmarkService.searchBookmarks(query, page);
    }

    //@Valid는 DataBinding을 체크해주는 Validator를 호출
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //201
    public BookmarkDTO createBookmark(@RequestBody @Valid CreateBookmarkRequest request) {
        return bookmarkService.createBookmark(request);
    }
}