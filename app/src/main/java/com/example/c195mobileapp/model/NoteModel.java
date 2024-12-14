package com.example.c195mobileapp.model;

public class NoteModel {
    private int noteID;
    private String noteTitle;
    private String noteContent;
    private int noteCourse;

    public NoteModel(int noteID, String noteTitle, int noteCourse, String noteContent) {
        this.noteID = noteID;
        this.noteTitle = noteTitle;
        this.noteCourse = noteCourse;
        this.noteContent = noteContent;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public int getNoteCourse() {
        return noteCourse;
    }

    public void setNoteCourse(int noteCourse) {
        this.noteCourse = noteCourse;
    }
}
