package com.faf.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.faf.project.model.AssistanceScheme;
import com.faf.project.model.Student;
import com.faf.project.service.AssistanceSchemeService;
import com.faf.project.service.NotificationService;
import com.faf.project.service.StudentService;

@RestController
public class EligibilityController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AssistanceSchemeService assistanceSchemeService;

    @Autowired
    private NotificationService notificationService;


    // ================= FIND ELIGIBLE SCHEMES =================

    @GetMapping("/eligible/{studentId}")
    public List<AssistanceScheme> getEligibleSchemes(
            @PathVariable Long studentId) {

        // Get student
        Student student =
                studentService.getStudentById(studentId);


        // Find matching schemes
        List<AssistanceScheme> eligibleSchemes =
                assistanceSchemeService
                        .getEligibleSchemes(student);


        // Create notifications
        for (AssistanceScheme scheme : eligibleSchemes) {

            notificationService
                    .createEligibilityNotification(
                            studentId,
                            scheme
                    );

            notificationService
                    .checkDeadlineAndNotify(
                            studentId,
                            scheme
                    );
        }


        return eligibleSchemes;
    }
}