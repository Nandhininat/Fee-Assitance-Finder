package com.faf.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.faf.project.exception.ResourceNotFoundException;
import com.faf.project.model.Student;
import com.faf.project.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // ================= SAVE STUDENT =================

    public Student saveStudent(Student student) {

        Student existing =
                studentRepository.findByUsername(
                        student.getUsername()
                );

        if (existing != null
                && existing.getStudentid()
                != student.getStudentid()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists"
            );
        }


        if (student.getPassword() != null
                && !student.getPassword().isBlank()) {

            student.setPassword(
                    passwordEncoder.encode(
                            student.getPassword()
                    )
            );
        }


        return studentRepository.save(student);
    }


    // ================= GET ALL =================

    public List<Student> getAllStudents() {

        return studentRepository.findAll();
    }


    // ================= GET BY ID =================

    public Student getStudentById(
            Long studentId) {

        return studentRepository.findById(studentId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        "Student not found with ID: "
                        + studentId
                    )
                );
    }


    // ================= LOGIN =================

    public Student login(
            String username,
            String password) {

        Student student =
                studentRepository.findByUsername(
                        username
                );


        if (student == null) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }


        if (student.getPassword() == null
                || !passwordEncoder.matches(
                        password,
                        student.getPassword()
                )) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }


        return student;
    }
}