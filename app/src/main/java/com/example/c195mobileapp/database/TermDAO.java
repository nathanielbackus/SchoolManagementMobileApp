package com.example.c195mobileapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.c195mobileapp.model.TermModel;

import java.util.ArrayList;
import java.util.List;

public class TermDAO {
    private final SQLiteOpenHelper dbHelper;

    public TermDAO(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean deleteTerm(int termID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("TERM_TABLE", "TERM_ID = ?", new String[]{String.valueOf(termID)});
        db.close();
        return result > 0;
    }

    public List<Integer> getAssociatedCourseIDs(int termID) {
        List<Integer> courseIDs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COURSE_ID FROM TERM_COURSE_TABLE WHERE TERM_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(termID)});
        if (cursor.moveToFirst()) {
            do {
                int courseID = cursor.getInt(cursor.getColumnIndexOrThrow("COURSE_ID"));
                courseIDs.add(courseID);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return courseIDs;
    }

    public List<TermModel> getAllTerms() {
        List<TermModel> termReturnList = new ArrayList<>();
        String queryString = "SELECT * FROM TERM_TABLE";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int termID = cursor.getInt(cursor.getColumnIndexOrThrow("TERM_ID"));
                String termTitle = cursor.getString(cursor.getColumnIndexOrThrow("TERM_TITLE"));
                String courseStart = cursor.getString(cursor.getColumnIndexOrThrow("START_DATE"));
                String courseEnd = cursor.getString(cursor.getColumnIndexOrThrow("END_DATE"));
                TermModel termModel = new TermModel(termID, termTitle, courseStart, courseEnd);
                termReturnList.add(termModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return termReturnList;
    }


    public boolean addTerm(TermModel termModel, List<Integer> associatedCourseIDs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put("TERM_TITLE", termModel.getTermTitle());
            cv.put("START_DATE", termModel.getTermStart());
            cv.put("END_DATE", termModel.getTermEnd());

            long termId = db.insert("TERM_TABLE", null, cv);
            if (termId == -1) {
                return false;
            }
            for (Integer courseID : associatedCourseIDs) {
                ContentValues courseCV = new ContentValues();
                courseCV.put("TERM_ID", termId);
                courseCV.put("COURSE_ID", courseID);
                //query for all course ids belonging to term id
                long courseInsert = db.insert("TERM_COURSE_TABLE", null, courseCV);
                if (courseInsert == -1) {
                    return false;
                }
            }
        } finally {
            db.close();
        }
        return true;
    }


    public boolean updateTerm(TermModel termModel, List<Integer> associatedCourseIDs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put("TERM_TITLE", termModel.getTermTitle());
            cv.put("START_DATE", termModel.getTermStart());
            cv.put("END_DATE", termModel.getTermEnd());

            int rowsAffected = db.update("TERM_TABLE", cv, "TERM_ID = ?", new String[]{String.valueOf(termModel.getTermID())});
            if (rowsAffected <= 0) {
                return false;
            }
            db.delete("TERM_COURSE_TABLE", "TERM_ID = ?", new String[]{String.valueOf(termModel.getTermID())});
            for (Integer courseID : associatedCourseIDs) {
                ContentValues courseCV = new ContentValues();
                courseCV.put("TERM_ID", termModel.getTermID());
                courseCV.put("COURSE_ID", courseID);
                long courseInsert = db.insert("TERM_COURSE_TABLE", null, courseCV);
                if (courseInsert == -1) {
                    return false;
                }
            }
        } finally {
            db.close();
        }
        return true;
    }
}