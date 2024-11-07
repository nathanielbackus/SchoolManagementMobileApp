package com.example.c195mobileapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.c195mobileapp.model.AssessmentModel;
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

    public List<CourseModel> getAllCourses() {
        List<CourseModel> courseReturnList = new ArrayList<>();
        String queryString = "SELECT * FROM COURSE_TABLE";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int courseID = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow("COURSE_TITLE"));
                String courseStart = cursor.getString(cursor.getColumnIndexOrThrow("START_DATE"));
                String courseEnd = cursor.getString(cursor.getColumnIndexOrThrow("END_DATE"));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow("STATUS"));
                int mentorID = cursor.getInt(cursor.getColumnIndexOrThrow("MENTOR_ID"));
                String associatedAssessmentsStr = cursor.getString(cursor.getColumnIndexOrThrow("ASSOCIATED_ASSESSMENTS"));

                CourseModel courseModel = new CourseModel(courseID, courseTitle, courseStart, courseEnd, status, mentorID, associatedAssessmentsStr);
                courseModel.setAssociatedAssessmentIDsFromString(associatedAssessmentsStr);

                courseReturnList.add(courseModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return courseReturnList;
    }


    public boolean addCourse(CourseModel courseModel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put("COURSE_TITLE", courseModel.getCourseTitle());
            cv.put("START_DATE", courseModel.getCourseStart());
            cv.put("END_DATE", courseModel.getCourseEnd());
            cv.put("STATUS", courseModel.getStatus());
            cv.put("MENTOR_ID", courseModel.getMentorid());
            cv.put("ASSOCIATED_ASSESSMENTS", courseModel.getAssociatedAssessmentIDsAsString());

            long courseId = db.insert("COURSE_TABLE", null, cv);
            return courseId != -1;
        } finally {
            db.close();
        }
    }



    public boolean updateCourse(CourseModel courseModel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put("COURSE_TITLE", courseModel.getCourseTitle());
            cv.put("START_DATE", courseModel.getCourseStart());
            cv.put("END_DATE", courseModel.getCourseEnd());
            cv.put("STATUS", courseModel.getStatus());
            cv.put("MENTOR_ID", courseModel.getMentorid());
            cv.put("ASSOCIATED_ASSESSMENTS", courseModel.getAssociatedAssessmentIDsAsString());

            int rowsAffected = db.update("COURSE_TABLE", cv, "ID = ?", new String[]{String.valueOf(courseModel.getCourseID())});
            return rowsAffected > 0;
        } finally {
            db.close();
        }
    }



}
