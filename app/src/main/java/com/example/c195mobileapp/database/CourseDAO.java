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

    public boolean deleteCourse(int courseID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("COURSE_TABLE", "COURSE_ID = ?", new String[]{String.valueOf(courseID)});
        db.close();
        return result > 0;
    }

    public List<Integer> getAssociatedAssessmentIDs(int courseID) {
        List<Integer> assessmentIDs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT ASSESSMENT_ID FROM COURSE_ASSESSMENT_TABLE WHERE COURSE_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courseID)});
        if (cursor.moveToFirst()) {
            do {
                int assessmentID = cursor.getInt(cursor.getColumnIndexOrThrow("ASSESSMENT_ID"));
                assessmentIDs.add(assessmentID);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return assessmentIDs;
    }

    public List<CourseModel> getAllCourses() {
        List<CourseModel> courseReturnList = new ArrayList<>();
        String queryString = "SELECT * FROM COURSE_TABLE";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int courseID = cursor.getInt(cursor.getColumnIndexOrThrow("COURSE_ID"));
                String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow("COURSE_TITLE"));
                String courseStart = cursor.getString(cursor.getColumnIndexOrThrow("START_DATE"));
                String courseEnd = cursor.getString(cursor.getColumnIndexOrThrow("END_DATE"));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow("STATUS"));
                int mentorID = cursor.getInt(cursor.getColumnIndexOrThrow("MENTOR_ID"));

                CourseModel courseModel = new CourseModel(courseID, courseTitle, courseStart, courseEnd, status, mentorID);

                courseReturnList.add(courseModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return courseReturnList;
    }


    public boolean addCourse(CourseModel courseModel, List<Integer> associatedAssessmentIDs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put("COURSE_TITLE", courseModel.getCourseTitle());
            cv.put("START_DATE", courseModel.getCourseStart());
            cv.put("END_DATE", courseModel.getCourseEnd());
            cv.put("STATUS", courseModel.getStatus());
            cv.put("MENTOR_ID", courseModel.getMentorID());

            long courseId = db.insert("COURSE_TABLE", null, cv);
            if (courseId == -1) {
                return false;
            }
            for (Integer assessmentID : associatedAssessmentIDs) {
                ContentValues assessmentCV = new ContentValues();
                assessmentCV.put("COURSE_ID", courseId);
                assessmentCV.put("ASSESSMENT_ID", assessmentID);
                //query for all assessment ids belonging to course id
                long assessmentInsert = db.insert("COURSE_ASSESSMENT_TABLE", null, assessmentCV);
                if (assessmentInsert == -1) {
                    return false;
                }
            }
        } finally {
            db.close();
        }
        return true;
    }


    public boolean updateCourse(CourseModel courseModel, List<Integer> associatedAssessmentIDs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put("COURSE_TITLE", courseModel.getCourseTitle());
            cv.put("START_DATE", courseModel.getCourseStart());
            cv.put("END_DATE", courseModel.getCourseEnd());
            cv.put("STATUS", courseModel.getStatus());
            cv.put("MENTOR_ID", courseModel.getMentorID());

            int rowsAffected = db.update("COURSE_TABLE", cv, "COURSE_ID = ?", new String[]{String.valueOf(courseModel.getCourseID())});
            if (rowsAffected <= 0) {
                return false;
            }
            db.delete("COURSE_ASSESSMENT_TABLE", "COURSE_ID = ?", new String[]{String.valueOf(courseModel.getCourseID())});
            for (Integer assessmentID : associatedAssessmentIDs) {
                ContentValues assessmentCV = new ContentValues();
                assessmentCV.put("COURSE_ID", courseModel.getCourseID());
                assessmentCV.put("ASSESSMENT_ID", assessmentID);
                long assessmentInsert = db.insert("COURSE_ASSESSMENT_TABLE", null, assessmentCV);
                if (assessmentInsert == -1) {
                    return false;
                }
            }
        } finally {
            db.close();
        }
        return true;
    }
}
