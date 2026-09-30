package com.faf.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faf.project.model.Student;

public interface StudentRepository
        extends JpaRepository<Student, Long> {

    Student findByUsername(String username);
}