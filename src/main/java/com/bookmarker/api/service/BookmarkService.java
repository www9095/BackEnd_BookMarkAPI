package com.bookmarker.api.service;

import com.bookmarker.api.domain.Bookmark;
import com.bookmarker.api.domain.BookmarkRepository;
import com.bookmarker.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor //생성자 추가하지 않아도 자동으로 생성되게 해줌
public class BookmarkService {
    private final BookmarkRepository repository;
    private final BookmarkMapper mapper;

    @Transactional(readOnly = true)
    public BookmarksDTO getBookmarks(Integer page) {
        //JPA의 페이지번호가 0부터 시작하기 때문에(클라이언트는 1부터)
        int pageNo = page < 1 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(pageNo, 4, Sort.Direction.DESC, "id");
//        Page<Bookmark> bookmarkPage = repository.findAll(pageable); //Page<T> //.getContent(); //page내부 데이터 추출
//        // Page<Bookmark> => Page<BookarkDto>
//        Page<BookmarkDTO> bookmarkPage = repository.findAll(pageable)
//                                    //map(Function) Function,의 추상메서드 R apploy(T t)
////                                   .map(bookmark -> mapper.toDTO(bookmark)); //lamda
//                                    //lamda method reference
//                                     .map(mapper::toDTO);
        //Query Method 호출
        Page<BookmarkDTO> bookmarkPage = repository.findBookmarks(pageable);

        return new BookmarksDTO(bookmarkPage);

    }

    @Transactional(readOnly = true)
    public BookmarksDTO<?> searchBookmarks(String query, Integer page) {
        int pageNo = page < 1 ? 0 : page - 1 ;
        Pageable pageable = PageRequest.of(pageNo, 4, Sort.Direction.DESC, "createdAt");
//        Page<BookmarkDTO> bookmarkPage = repository.searchBookmarks(query, pageable);
//        Page<BookmarkDTO> bookmarkPage = repository.findByTitleContainsIgnoreCase(query, pageable);
        Page<BookmarkVM> bookmarkPage = repository.findByTitleContainingIgnoreCase(query, pageable);
        return new BookmarksDTO<>(bookmarkPage);
    }
    
    //class transaction 종속
    public BookmarkDTO createBookmark(CreateBookmarkRequest request) {
        Bookmark bookmark = new Bookmark(request.getTitle(), request.getUrl(), Instant.now());
        Bookmark savedBookmark = repository.save(bookmark);
        return mapper.toDTO(savedBookmark);
    }
}