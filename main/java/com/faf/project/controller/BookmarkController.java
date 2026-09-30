package com.faf.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.faf.project.model.Bookmark;
import com.faf.project.service.BookmarkService;

@RestController
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;


    // ================= SAVE BOOKMARK =================

    @PostMapping("/bookmark")
    public Bookmark saveBookmark(
            @RequestBody Bookmark bookmark) {

        return bookmarkService.saveBookmark(bookmark);
    }


    // ================= GET ALL BOOKMARKS =================

    @GetMapping("/bookmark")
    public List<Bookmark> getAllBookmarks() {

        return bookmarkService.getAllBookmarks();
    }


    // ================= GET STUDENT BOOKMARKS =================

    @GetMapping("/bookmark/student/{studentId}")
    public List<Bookmark> getBookmarksByStudentId(
            @PathVariable Long studentId) {

        return bookmarkService
                .getBookmarksByStudentId(studentId);
    }


    // ================= GET SPECIFIC BOOKMARK =================

    @GetMapping(
        "/bookmark/student/{studentId}/scheme/{schemeId}"
    )
    public Bookmark getBookmark(
            @PathVariable Long studentId,
            @PathVariable Long schemeId) {

        return bookmarkService.getBookmark(
                studentId,
                schemeId
        );
    }


    // ================= DELETE BOOKMARK =================

    @DeleteMapping("/bookmark/{bookmarkId}")
    public String deleteBookmark(
            @PathVariable Long bookmarkId) {

        bookmarkService.deleteBookmark(bookmarkId);

        return "Bookmark deleted successfully";
    }
}