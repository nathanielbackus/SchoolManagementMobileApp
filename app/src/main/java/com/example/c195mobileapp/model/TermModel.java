package com.example.c195mobileapp.model;

import java.util.List;

public class TermModel {
    private int termID;
    private String termTitle;
    private String termStart;
    private String termEnd;
    private List<CourseModel> associatedCourses;

    public TermModel(int termID, String termTitle, String termStart, String termEnd) {
        this.termID = termID;
        this.termTitle = termTitle;
        this.termStart = termStart;
        this.termEnd = termEnd;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    public String getTermStart() {
        return termStart;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    public void addAssociatedCourse(CourseModel course) {
        associatedCourses.add(course);
    }

    public void deleteAssociatedCourse(CourseModel course) {
        associatedCourses.remove(course);
    }

    public List<CourseModel> getAllAssociatedCourses() {
        return associatedCourses;
    }
}
