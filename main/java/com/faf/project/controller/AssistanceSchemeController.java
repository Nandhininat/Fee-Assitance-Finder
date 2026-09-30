package com.faf.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.faf.project.model.AssistanceScheme;
import com.faf.project.service.AssistanceSchemeService;

import jakarta.validation.Valid;

@RestController
public class AssistanceSchemeController {

    @Autowired
    private AssistanceSchemeService assistanceSchemeService;


    // ================= CREATE SCHEME =================

    @PostMapping("/scheme")
    public AssistanceScheme saveScheme(
            @Valid @RequestBody AssistanceScheme scheme) {

        return assistanceSchemeService.saveScheme(scheme);
    }


    // ================= GET ALL SCHEMES =================

    @GetMapping("/scheme")
    public List<AssistanceScheme> getAllSchemes() {

        return assistanceSchemeService.getAllSchemes();
    }


    // ================= GET SCHEME BY ID =================

    @GetMapping("/scheme/{schemeId}")
    public AssistanceScheme getSchemeById(
            @PathVariable Long schemeId) {

        return assistanceSchemeService.getSchemeById(schemeId);
    }


    // ================= SEARCH SCHEME =================

    @GetMapping("/scheme/search")
    public List<AssistanceScheme> searchSchemes(
            @RequestParam String name) {

        return assistanceSchemeService.searchSchemes(name);
    }


    // ================= ACTIVE SCHEMES =================

    @GetMapping("/scheme/active")
    public List<AssistanceScheme> getActiveSchemes() {

        return assistanceSchemeService.getActiveSchemes();
    }


    // ================= EXPIRED SCHEMES =================

    @GetMapping("/scheme/expired")
    public List<AssistanceScheme> getExpiredSchemes() {

        return assistanceSchemeService.getExpiredSchemes();
    }


    // ================= UPDATE SCHEME =================

    @PutMapping("/scheme/{schemeId}")
    public AssistanceScheme updateScheme(
            @PathVariable Long schemeId,
            @Valid @RequestBody AssistanceScheme scheme) {

        return assistanceSchemeService.updateScheme(
                schemeId,
                scheme
        );
    }


    // ================= DELETE SCHEME =================

    @DeleteMapping("/scheme/{schemeId}")
    public String deleteScheme(
            @PathVariable Long schemeId) {

        assistanceSchemeService.deleteScheme(schemeId);

        return "Scheme deleted successfully";
    }
}