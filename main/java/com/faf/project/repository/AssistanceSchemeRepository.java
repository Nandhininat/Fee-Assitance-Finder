package com.faf.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faf.project.model.AssistanceScheme;

public interface AssistanceSchemeRepository
        extends JpaRepository<AssistanceScheme, Long> {

    List<AssistanceScheme>
    findBySchemeNameContainingIgnoreCase(
            String schemeName);
}