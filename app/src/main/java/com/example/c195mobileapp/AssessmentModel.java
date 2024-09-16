package com.example.c195mobileapp;

public class AssessmentModel {
    private int AssessmentID;
    private String AssessmentTitle;
    private String AssessmentStart;
    private String AssessmentEnd;
    private boolean AssessmentType;

    public AssessmentModel(int assessmentID, String assessmentTitle, String assessmentStart, String assessmentEnd, boolean assessmentType) {
        AssessmentID = assessmentID;
        AssessmentTitle = assessmentTitle;
        AssessmentStart = assessmentStart;
        AssessmentEnd = assessmentEnd;
        this.AssessmentType = assessmentType;
    }

    public int getAssessmentID() {
        return AssessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        AssessmentID = assessmentID;
    }

    public String getAssessmentTitle() {
        return AssessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        AssessmentTitle = assessmentTitle;
    }

    public String getAssessmentStart() {
        return AssessmentStart;
    }

    public void setAssessmentStart(String assessmentStart) {AssessmentStart = assessmentStart;}

    public String getAssessmentEnd() {return AssessmentEnd;}

    public void setAssessmentEnd(String assessmentEnd) {
        AssessmentEnd = assessmentEnd;
    }

    public boolean getAssessmentType() {
        return AssessmentType;
    }

    public void setAssessmentType(boolean assessmentType) {
        AssessmentType = assessmentType;
    }

    @Override
    public String toString() {
        return "AssessmentModel{" +
                "AssessmentID=" + AssessmentID +
                ", AssessmentTitle='" + AssessmentTitle + '\'' +
                ", AssessmentStart='" + AssessmentStart + '\'' +
                ", AssessmentEnd='" + AssessmentEnd + '\'' +
                ", isObjective=" + AssessmentType +
                '}';
    }
}
