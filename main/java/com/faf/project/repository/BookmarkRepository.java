package com.faf.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faf.project.model.Bookmark;

public interface BookmarkRepository
        extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByStudentId(
            Long studentId);

    Bookmark findByStudentIdAndSchemeId(
            Long studentId,
            Long schemeId);
}