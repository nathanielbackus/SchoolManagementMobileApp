package com.example.c195mobileapp.database;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.c195mobileapp.model.MentorModel;

import java.util.ArrayList;
import java.util.List;


public class MentorDAO {
    private final SQLiteOpenHelper dbHelper;
    public MentorDAO (SQLiteOpenHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    public boolean deleteMentor(int mentorID){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        int result = db.delete("MENTOR_TABLE", "MENTOR_ID = ?", new String[]{String.valueOf(mentorID)});
        db.close();
        return result > 0;
    }

    public boolean addMentor(MentorModel mentorModel){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MENTOR_NAME", mentorModel.getMentorName());
        cv.put("MENTOR_EMAIL", mentorModel.getMentorEmail());
        cv.put("MENTOR_PHONE", mentorModel.getMentorPhone());
        long insert = db.insert("MENTOR_TABLE", null, cv);
        db.close();
        return insert != -1;
    }

    public boolean updateMentor(MentorModel mentorModel){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MENTOR_NAME", mentorModel.getMentorName());
        cv.put("MENTOR_EMAIL", mentorModel.getMentorEmail());
        cv.put("MENTOR_PHONE", mentorModel.getMentorPhone());
        int update = db.update("MENTOR_TABLE", cv, "MENTOR_ID = ?", new String[]{String.valueOf(mentorModel.getMentorID())});
        db.close();
        return update > 0;
    }

    public List<MentorModel> getAllMentors() {
        List<MentorModel> mentorReturnList = new ArrayList<>();
        String queryString = "SELECT * FROM MENTOR_TABLE";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int mentorID = cursor.getInt(0);
                String mentorName = cursor.getString(1);
                String mentorEmail = cursor.getString(2);
                String mentorPhone = cursor.getString(3);

                MentorModel mentorModel = new MentorModel(mentorID, mentorName, mentorEmail, mentorPhone);
                mentorReturnList.add(mentorModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return mentorReturnList;
    }
}
