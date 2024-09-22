package com.example.c195mobileapp.model;

import java.util.List;


public class CourseModel {
    private List<AssessmentModel> associatedAssessments;
    private int courseID;
    private String courseTitle;
    private String courseStart;
    private String courseEnd;
    public CourseModel (int courseID, String courseTitle, String courseStart, String courseEnd) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(String courseStart) {
        this.courseStart = courseStart;
    }

    public String getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(String courseEnd) {
        this.courseEnd = courseEnd;
    }
    public void addAssociatedAssessment(AssessmentModel assessment) {
        associatedAssessments.add(assessment);
    }
    public void deleteAssociatedAssessment(AssessmentModel assessment) {
        associatedAssessments.remove(assessment);
    }
    public List<AssessmentModel> getAllAssociatedAssessments() {
        return associatedAssessments;
    }
}
//has title, dates, and a list of assessments
//check if assessments are out of range of course dates