package com.faf.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faf.project.model.Admin;

public interface AdminRepository
        extends JpaRepository<Admin, Long> {

    Admin findByUsername(String username);
}