package com.example.c195mobileapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.c195mobileapp.model.AssessmentModel;

import java.util.ArrayList;
import java.util.List;

public class AssessmentDAO {
    private final SQLiteOpenHelper dbHelper;

    public AssessmentDAO(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    public boolean deleteAssessment(AssessmentModel assessmentModel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("ASSESSMENT_TABLE", "ID = ?", new String[]{String.valueOf(assessmentModel.getAssessmentID())});
        db.close();
        return result > 0;
    }

    public boolean addAssessment(AssessmentModel assessmentModel){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ASSESSMENT_TITLE", assessmentModel.getAssessmentTitle());
        cv.put("START_DATE", assessmentModel.getAssessmentStart());
        cv.put("END_DATE", assessmentModel.getAssessmentEnd());
        cv.put("ASSESSMENT_TYPE", assessmentModel.getAssessmentType() ? 1 : 0);
        long insert = db.insert("ASSESSMENT_TABLE", null, cv);
        db.close();
        return insert != -1;
    }
    public boolean updateAssessment(AssessmentModel assessmentModel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ASSESSMENT_TITLE", assessmentModel.getAssessmentTitle());
        cv.put("START_DATE", assessmentModel.getAssessmentStart());
        cv.put("END_DATE", assessmentModel.getAssessmentEnd());
        cv.put("ASSESSMENT_TYPE", assessmentModel.getAssessmentType() ? 1 : 0);
        int update = db.update("ASSESSMENT_TABLE", cv, "ID = ?", new String[]{String.valueOf(assessmentModel.getAssessmentID())});
        db.close();
        return update > 0;
    }


    public List<AssessmentModel> getAllAppointments() {
        List<AssessmentModel> assessmentReturnList = new ArrayList<>();
        String queryString = "SELECT * FROM ASSESSMENT_TABLE";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
