package com.example.c195mobileapp.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.c195mobileapp.database.AssessmentDAO;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context) {
        super(context, "assessment.db", null, 1);
    }

    // called the first time a database is accessed. code in here creates a new databse
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAssessmentTable = "CREATE TABLE ASSESSMENT_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, ASSESSMENT_TITLE TEXT, START_DATE TEXT, END_DATE TEXT, ASSESSMENT_TYPE INTEGER)";
        String createNoteTable = "CREATE TABLE NOTE_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOTE_TITLE TEXT, NOTE_CONTENT TEXT)";
        String createCourseTable = "CREATE TABLE COURSE_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, COURSE_TITLE TEXT, START_DATE TEXT, END_DATE TEXT, STATUS TEXT, MENTOR TEXT)";
        String createTermTable = "CREATE TABLE TERM_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, TERM_TITLE TEXT, START_DATE TEXT, END_DATE TEXT)";
        String createMentorTable = "CREATE TABLE MENTOR_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, MENTOR_NAME TEXT, MENTOR_EMAIL TEXT, MENTOR_PHONE TEXT)";
        db.execSQL(createAssessmentTable);
        db.execSQL(createNoteTable);
        db.execSQL(createCourseTable);
        db.execSQL(createTermTable);
        db.execSQL(createMentorTable);
    }
    // this is called if the database version number changes. prevents errors when you change database design.
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS ASSESSMENT_TABLE");
        db.execSQL("DROP TABLE IF EXISTS NOTE_TABLE");
        db.execSQL("DROP TABLE IF EXISTS COURSE_TABLE");
        db.execSQL("DROP TABLE IF EXISTS TERM_TABLE");
        db.execSQL("DROP TABLE IF EXISTS MENTOR_TABLE");
        onCreate(db);
    }



}
