package com.example.c195mobileapp.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.c195mobileapp.model.AssessmentModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String ASSESSMENT_TABLE = "ASSESSMENT_TABLE";
    public static final String COLUMN_ASSESSMENT_TITLE = "ASSESSMENT_TITLE";
    public static final String COLUMN_START_DATE = "START_DATE";
    public static final String COLUMN_END_DATE = "END_DATE";
    public static final String COLUMN_ASSESSMENT_TYPE = "ASSESSMENT_TYPE";
    public static final String COLUMN_ID = "ID";


    public DataBaseHelper(@Nullable Context context) {
        super(context, "assessment.db", null, 1);
    }

    // called the first time a database is accessed. code in here creates a new databse
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + ASSESSMENT_TABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ASSESSMENT_TITLE + " TEXT, "
                + COLUMN_START_DATE + " TEXT, "  // Assuming date is stored as TEXT
                + COLUMN_END_DATE + " TEXT, "
                + COLUMN_ASSESSMENT_TYPE + " INTEGER);"

                "CREATE TABLE " + COURSE_TABLE + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_COURSE_TITLE + " TEXT, "
                        + COLUMN_START_DATE + " TEXT, "  // Assuming date is stored as TEXT
                        + COLUMN_END_DATE + " TEXT, "

    ";
        db.execSQL(createTableStatement);
    }
    // this is called if the database version number changes. prevents errors when you change database design.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public boolean deleteAssessment(AssessmentModel assessmentModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + ASSESSMENT_TABLE + " WHERE " + COLUMN_ID + " = " + assessmentModel.getAssessmentID();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addAssessment(AssessmentModel assessmentModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ASSESSMENT_TITLE, assessmentModel.getAssessmentTitle());
        cv.put(COLUMN_START_DATE, assessmentModel.getAssessmentStart());
        cv.put(COLUMN_END_DATE, assessmentModel.getAssessmentEnd());
        cv.put(COLUMN_ASSESSMENT_TYPE, assessmentModel.getAssessmentType());
        long insert = db.insert(ASSESSMENT_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean updateAssessment(AssessmentModel assessmentModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ASSESSMENT_TITLE, assessmentModel.getAssessmentTitle());
        cv.put(COLUMN_START_DATE, assessmentModel.getAssessmentStart());
        cv.put(COLUMN_END_DATE, assessmentModel.getAssessmentEnd());
        cv.put(COLUMN_ASSESSMENT_TYPE, assessmentModel.getAssessmentType() ? 1 : 0);
        int update = db.update(ASSESSMENT_TABLE, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(assessmentModel.getAssessmentID())});
        db.close();
        return update > 0;
    }


    public List<AssessmentModel> getAllAppointments() {
        List<AssessmentModel> assessmentReturnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + ASSESSMENT_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            do {
                // Fetching data from each row
                int assessmentID = cursor.getInt(0);          // Assuming column 0 is the assessment ID
                String assessmentTitle = cursor.getString(1); // Assuming column 1 is the assessment title
                String assessmentStart = cursor.getString(2); // Assuming column 2 is the assessment start date
                String assessmentEnd = cursor.getString(3);   // Assuming column 3 is the assessment end date
                boolean assessmentType = cursor.getInt(4) == 1; // Assuming column 4 stores a boolean as an integer (0 or 1)

                // Create a new AssessmentModel and add it to the list
                AssessmentModel assessmentModel = new AssessmentModel(assessmentID, assessmentTitle, assessmentStart, assessmentEnd, assessmentType);
                assessmentReturnList.add(assessmentModel);
            } while (cursor.moveToNext()); // Continue looping until there are no more rows
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return assessmentReturnList; // Return the populated list
    }



}
