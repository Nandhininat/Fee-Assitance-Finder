package com.faf.project.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class AssistanceScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long schemeID;

    @NotBlank(message = "Scheme name is required")
    private String schemeName;

    @NotBlank(message = "Benefits are required")
    private String benefits;

    private LocalDate applicationDeadline;

    @NotBlank(message = "Eligibility details are required")
    private String eligibility;

    @NotBlank(message = "Application link is required")
    private String applicationLink;

    @NotBlank(message = "Required documents are required")
    private String requiredDocuments;

    @NotBlank(message = "Application procedure is required")
    private String applicationProcedure;

    @PositiveOrZero(message = "Income limit cannot be negative")
    private double incomeLimit;

    @NotBlank(message = "Eligible category is required")
    private String eligibleCategory;

    @NotBlank(message = "Eligible course is required")
    private String eligibleCourse;

    @NotBlank(message = "Eligible location is required")
    private String eligibleLocation;


    public long getSchemeID() {
        return schemeID;
    }

    public void setSchemeID(long schemeID) {
        this.schemeID = schemeID;
    }


    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }


    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }


    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(
            LocalDate applicationDeadline) {

        this.applicationDeadline = applicationDeadline;
    }


    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }


    public String getApplicationLink() {
        return applicationLink;
    }

    public void setApplicationLink(String applicationLink) {
        this.applicationLink = applicationLink;
    }


    public String getRequiredDocuments() {
        return requiredDocuments;
    }

    public void setRequiredDocuments(
            String requiredDocuments) {

        this.requiredDocuments = requiredDocuments;
    }


    public String getApplicationProcedure() {
        return applicationProcedure;
    }

    public void setApplicationProcedure(
            String applicationProcedure) {

        this.applicationProcedure = applicationProcedure;
    }


    public double getIncomeLimit() {
        return incomeLimit;
    }

    public void setIncomeLimit(double incomeLimit) {
        this.incomeLimit = incomeLimit;
    }


    public String getEligibleCategory() {
        return eligibleCategory;
    }

    public void setEligibleCategory(
            String eligibleCategory) {

        this.eligibleCategory = eligibleCategory;
    }


    public String getEligibleCourse() {
        return eligibleCourse;
    }

    public void setEligibleCourse(
            String eligibleCourse) {

        this.eligibleCourse = eligibleCourse;
    }


    public String getEligibleLocation() {
        return eligibleLocation;
    }

    public void setEligibleLocation(
            String eligibleLocation) {

        this.eligibleLocation = eligibleLocation;
    }
}