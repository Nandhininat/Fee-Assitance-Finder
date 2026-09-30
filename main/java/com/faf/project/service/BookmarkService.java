package com.faf.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faf.project.exception.ResourceNotFoundException;
import com.faf.project.model.Bookmark;
import com.faf.project.repository.AssistanceSchemeRepository;
import com.faf.project.repository.BookmarkRepository;
import com.faf.project.repository.StudentRepository;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AssistanceSchemeRepository assistanceSchemeRepository;


    // ================= SAVE =================

    public Bookmark saveBookmark(Bookmark bookmark) {

        // Check whether student exists
        if (!studentRepository.existsById(bookmark.getStudentId())) {
            throw new ResourceNotFoundException(
                    "Student not found with ID: "
                    + bookmark.getStudentId()
            );
        }

        // Check whether scheme exists
        if (!assistanceSchemeRepository.existsById(bookmark.getSchemeId())) {
            throw new ResourceNotFoundException(
                    "Scheme not found with ID: "
                    + bookmark.getSchemeId()
            );
        }

        // Check duplicate bookmark
        Bookmark existingBookmark =
                bookmarkRepository.findByStudentIdAndSchemeId(
                        bookmark.getStudentId(),
                        bookmark.getSchemeId()
                );

        if (existingBookmark != null) {
            return existingBookmark;
        }

        return bookmarkRepository.save(bookmark);
    }


    // ================= GET ALL =================

    public List<Bookmark> getAllBookmarks() {

        return bookmarkRepository.findAll();
    }


    // ================= GET BY STUDENT =================

    public List<Bookmark> getBookmarksByStudentId(Long studentId) {

        return bookmarkRepository.findByStudentId(studentId);
    }


    // ================= GET SPECIFIC =================

    public Bookmark getBookmark(
            Long studentId,
            Long schemeId) {

        return bookmarkRepository.findByStudentIdAndSchemeId(
                studentId,
                schemeId
        );
    }


    // ================= DELETE =================

    public void deleteBookmark(Long bookmarkId) {

        if (!bookmarkRepository.existsById(bookmarkId)) {
            throw new ResourceNotFoundException(
                    "Bookmark not found with ID: " + bookmarkId
            );
        }

        bookmarkRepository.deleteById(bookmarkId);
    }
}