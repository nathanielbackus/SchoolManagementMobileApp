package com.example.c195mobileapp.model;

import java.util.List;

public class CourseModel {
    private int courseID;
    private String courseTitle;
    private String courseStart;
    private String courseEnd;
    private int status;
    private int mentorID;

    // Constructor
    public CourseModel(int courseID, String courseTitle, String courseStart, String courseEnd, int status, int mentorID) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.status = status;
        this.mentorID = mentorID;
    }

    // Getters and Setters
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMentorID() {
        return mentorID;
    }

    public void setMentorID(int mentorID) {
        this.mentorID = mentorID;
    }

    // Placeholder methods for assessments; fetches should occur in DAO methods instead
    public List<Integer> getAssociatedAssessmentIDs() {
        // To be implemented in the DAO layer as needed
        throw new UnsupportedOperationException("Use DAO methods to fetch associated assessments.");
    }

    public void setAssociatedAssessments(List<Integer> associatedAssessmentIDs) {
        // To be implemented in the DAO layer as needed
        throw new UnsupportedOperationException("Use DAO methods to set associated assessments.");
    }
//maybe a check to find out if they are within dates
}
