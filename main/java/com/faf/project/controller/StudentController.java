package com.faf.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.faf.project.model.Student;
import com.faf.project.service.StudentService;

import jakarta.validation.Valid;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;


    // ================= REGISTER STUDENT =================

    @PostMapping("/student")
    public Student saveStudent(
            @Valid @RequestBody Student student) {

        return studentService.saveStudent(student);
    }


    // ================= GET ALL STUDENTS =================

    @GetMapping("/student")
    public List<Student> getAllStudents() {

        return studentService.getAllStudents();
    }


    // ================= STUDENT LOGIN =================

    @PostMapping("/login")
    public Student login(
            @RequestBody Student student) {

        return studentService.login(
                student.getUsername(),
                student.getPassword()
        );
    }
}