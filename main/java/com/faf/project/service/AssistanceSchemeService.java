package com.faf.project.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faf.project.exception.ResourceNotFoundException;
import com.faf.project.model.AssistanceScheme;
import com.faf.project.model.Student;
import com.faf.project.repository.AssistanceSchemeRepository;

@Service
public class AssistanceSchemeService {

    @Autowired
    private AssistanceSchemeRepository
            assistanceSchemeRepository;


    // ================= SAVE =================

    public AssistanceScheme saveScheme(
            AssistanceScheme scheme) {

        return assistanceSchemeRepository.save(
                scheme
        );
    }


    // ================= GET ALL =================

    public List<AssistanceScheme> getAllSchemes() {

        return assistanceSchemeRepository.findAll();
    }


    // ================= GET BY ID =================

    public AssistanceScheme getSchemeById(
            Long schemeId) {

        return assistanceSchemeRepository
                .findById(schemeId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        "Scheme not found with ID: "
                        + schemeId
                    )
                );
    }


    // ================= SEARCH =================

    public List<AssistanceScheme> searchSchemes(
            String name) {

        return assistanceSchemeRepository
                .findBySchemeNameContainingIgnoreCase(
                        name
                );
    }


    // ================= ELIGIBILITY =================

    public List<AssistanceScheme>
    getEligibleSchemes(Student student) {

        List<AssistanceScheme> schemes =
                assistanceSchemeRepository.findAll();


        return schemes.stream()
                .filter(scheme -> {

                    boolean incomeMatch =
                            student.getIncome()
                            <= scheme.getIncomeLimit();


                    boolean categoryMatch =
                            student.getCategory() != null
                            && scheme.getEligibleCategory() != null
                            && student.getCategory()
                                .trim()
                                .equalsIgnoreCase(
                                    scheme.getEligibleCategory()
                                        .trim()
                                );


                    boolean courseMatch =
                            student.getCourse() != null
                            && scheme.getEligibleCourse() != null
                            && student.getCourse()
                                .trim()
                                .equalsIgnoreCase(
                                    scheme.getEligibleCourse()
                                        .trim()
                                );


                    boolean locationMatch =
                            student.getLocation() != null
                            && scheme.getEligibleLocation() != null
                            && student.getLocation()
                                .trim()
                                .equalsIgnoreCase(
                                    scheme.getEligibleLocation()
                                        .trim()
                                );


                    return incomeMatch
                            && categoryMatch
                            && courseMatch
                            && locationMatch;

                })
                .toList();
    }


    // ================= ACTIVE =================

    public List<AssistanceScheme>
    getActiveSchemes() {

        LocalDate today =
                LocalDate.now();


        return assistanceSchemeRepository
                .findAll()
                .stream()
                .filter(scheme ->

                    scheme.getApplicationDeadline()
                            != null

                    && !scheme
                        .getApplicationDeadline()
                        .isBefore(today)

                )
                .toList();
    }


    // ================= EXPIRED =================

    public List<AssistanceScheme>
    getExpiredSchemes() {

        LocalDate today =
                LocalDate.now();


        return assistanceSchemeRepository
                .findAll()
                .stream()
                .filter(scheme ->

                    scheme.getApplicationDeadline()
                            != null

                    && scheme
                        .getApplicationDeadline()
                        .isBefore(today)

                )
                .toList();
    }


    // ================= UPDATE =================

    public AssistanceScheme updateScheme(
            Long schemeId,
            AssistanceScheme scheme) {

        AssistanceScheme existingScheme =
                assistanceSchemeRepository
                .findById(schemeId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        "Scheme not found with ID: "
                        + schemeId
                    )
                );


        existingScheme.setSchemeName(
                scheme.getSchemeName()
        );

        existingScheme.setBenefits(
                scheme.getBenefits()
        );

        existingScheme.setApplicationDeadline(
                scheme.getApplicationDeadline()
        );

        existingScheme.setEligibility(
                scheme.getEligibility()
        );

        existingScheme.setApplicationLink(
                scheme.getApplicationLink()
        );

        existingScheme.setRequiredDocuments(
                scheme.getRequiredDocuments()
        );

        existingScheme.setApplicationProcedure(
                scheme.getApplicationProcedure()
        );

        existingScheme.setIncomeLimit(
                scheme.getIncomeLimit()
        );

        existingScheme.setEligibleCategory(
                scheme.getEligibleCategory()
        );

        existingScheme.setEligibleCourse(
                scheme.getEligibleCourse()
        );

        existingScheme.setEligibleLocation(
                scheme.getEligibleLocation()
        );


        return assistanceSchemeRepository.save(
                existingScheme
        );
    }


    // ================= DELETE =================

    public void deleteScheme(
            Long schemeId) {

        AssistanceScheme scheme =
                assistanceSchemeRepository
                .findById(schemeId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        "Scheme not found with ID: "
                        + schemeId
                    )
                );


        assistanceSchemeRepository.delete(
                scheme
        );
    }
}