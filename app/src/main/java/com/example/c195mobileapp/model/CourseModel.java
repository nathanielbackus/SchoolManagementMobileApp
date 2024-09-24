package com.example.c195mobileapp.model;

import java.util.List;


public class CourseModel {
    private List<AssessmentModel> associatedAssessments;
    private List<NoteModel> associatedNotes;
    private int courseID;
    private String courseTitle;
    private String courseStart;
    private String courseEnd;
    private String status;
    private String mentor;


    public CourseModel(int courseID, String courseTitle, String courseStart, String courseEnd, String status, String mentor) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.status = status;
        this.mentor = mentor;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMentor() {
        return mentor;
    }
    public void setMentor(String mentor) {
        this.mentor = mentor;
    }
    public void addAssociatedNote(NoteModel note) {
        associatedNotes.add(note);
    }
    public void deleteAssociatedNote(NoteModel note) {
        associatedNotes.remove(note);
    }
    public List<NoteModel> getAllAssociatedNotes() {
        return associatedNotes;
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
//check if assessments are out of range of course dates