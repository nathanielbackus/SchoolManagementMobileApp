package com.example.c195mobileapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.c195mobileapp.model.CourseModel;

import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private final SQLiteOpenHelper dbHelper;
    public CourseDAO(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    public boolean deleteCourse(CourseModel courseModel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("COURSE_TABLE", "ID = ?", new String[]{String.valueOf(courseModel.getCourseID())});
        db.close();
        return result > 0;
    }

//    public boolean addCourse(CourseModel courseModel) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("COURSE_TITLE", courseModel.getCourseTitle());
//        cv.put("START_DATE", courseModel.getCourseStart());
//        cv.put("END_DATE", courseModel.getCourseEnd());
//        AssessmentModel selectedAssessment = assessmentListView.getSelectedAssessment();
//        if (selectedAssessment != null) {
//            cv.put("ASSESSMENT_ID", selectedAssessment.getAssessmentID());
//        }
//        long insert = db.insert("COURSE_TABLE", null, cv);
//        db.close();
//    }
}
