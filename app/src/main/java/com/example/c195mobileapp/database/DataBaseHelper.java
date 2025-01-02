package com.example.c195mobileapp.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context) {
        super(context, "assessment.db", null, 1);
    }

    // called the first time a database is accessed. code in here creates a new databse
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAssessmentTable =
                "CREATE TABLE ASSESSMENT_TABLE (" +
                        "ASSESSMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ASSESSMENT_TITLE TEXT, " +
                        "START_DATE TEXT, " +
                        "END_DATE TEXT, " +
                        "ASSESSMENT_TYPE INTEGER)";

        String createNoteTable =
                "CREATE TABLE NOTE_TABLE (" +
                        "NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NOTE_TITLE TEXT, " +
                        "NOTE_COURSE INTEGER, " +
                        "NOTE_CONTENT TEXT)";

        String createCourseTable =
                "CREATE TABLE COURSE_TABLE (" +
                        "COURSE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "COURSE_TITLE TEXT, " +
                        "START_DATE TEXT, " +
                        "END_DATE TEXT, " +
                        "STATUS TEXT, " +
                        "MENTOR_ID INTEGER, " +
                        "FOREIGN KEY (MENTOR_ID) REFERENCES MENTOR_TABLE(ID))";

        String createCourseAssessmentTable =
                "CREATE TABLE COURSE_ASSESSMENT_TABLE (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "COURSE_ID INTEGER, " +
                        "ASSESSMENT_ID INTEGER, " +
                        "FOREIGN KEY (COURSE_ID) REFERENCES COURSE_TABLE(ID) ON DELETE CASCADE, " +
                        "FOREIGN KEY (ASSESSMENT_ID) REFERENCES ASSESSMENT_TABLE(ID) ON DELETE CASCADE)";

        String createTermCourseTable =
                "CREATE TABLE TERM_COURSE_TABLE(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "TERM_ID INTEGER, " +
                        "COURSE_ID INTEGER, " +
                        "FOREIGN KEY (TERM_ID) REFERENCES TERM_TABLE(ID) ON DELETE CASCADE, " +
                        "FOREIGN KEY (COURSE_ID) REFERENCES COURSE_TABLE(ID) ON DELETE CASCADE)";

        String createTermTable =
                "CREATE TABLE TERM_TABLE (" +
                        "TERM_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "TERM_TITLE TEXT, " +
                        "START_DATE TEXT, " +
                        "END_DATE TEXT)";

        String createMentorTable =
                "CREATE TABLE MENTOR_TABLE (" +
                        "MENTOR_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "MENTOR_NAME TEXT, " +
                        "MENTOR_EMAIL TEXT, " +
                        "MENTOR_PHONE TEXT)";

        db.execSQL(createAssessmentTable);
        db.execSQL(createNoteTable);
        db.execSQL(createCourseTable);
        db.execSQL(createCourseAssessmentTable);
        db.execSQL(createTermCourseTable);
        db.execSQL(createTermTable);
        db.execSQL(createMentorTable);
    }

    // this is called if the database version number changes. prevents errors when you change database design.
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS ASSESSMENT_TABLE");
        db.execSQL("DROP TABLE IF EXISTS NOTE_TABLE");
        db.execSQL("DROP TABLE IF EXISTS COURSE_TABLE");
        db.execSQL("DROP TABLE IF EXISTS COURSE_ASSESSMENT_TABLE");
        db.execSQL("DROP TABLE IF EXISTS TERM_COURSE_TABLE");
        db.execSQL("DROP TABLE IF EXISTS TERM_TABLE");
        db.execSQL("DROP TABLE IF EXISTS MENTOR_TABLE");
        onCreate(db);
    }

    public String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    public String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + ", " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "JAN";
        }
    }

    public long getTimeInMillis(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
            Date date = sdf.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 8);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
