package com.example.c195mobileapp.database;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.c195mobileapp.model.CourseModel;
import com.example.c195mobileapp.model.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class NoteDAO {
    private final SQLiteOpenHelper dbHelper;

    public NoteDAO(SQLiteOpenHelper dbHelper) {this.dbHelper = dbHelper;}

    public boolean deleteNote(int noteID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("NOTE_TABLE", "NOTE_ID = ?", new String[]{String.valueOf(noteID)});
        db.close();
        return result > 0;
    }

    public List<NoteModel> getAllNotes() {
        List<NoteModel> noteReturnList = new ArrayList<>();
        String queryString = "SELECT * FROM NOTE_TABLE";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int noteID = cursor.getInt(cursor.getColumnIndexOrThrow("NOTE_ID"));
                String noteTitle = cursor.getString(cursor.getColumnIndexOrThrow("NOTE_TITLE"));
                int noteCourse = cursor.getInt(cursor.getColumnIndexOrThrow("NOTE_COURSE"));
                String noteContent = cursor.getString(cursor.getColumnIndexOrThrow("NOTE_CONTENT"));

                NoteModel noteModel = new NoteModel(noteID, noteTitle, noteCourse, noteContent);

                noteReturnList.add(noteModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return noteReturnList;
    }

    public boolean addNote(NoteModel noteModel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put("NOTE_TITLE", noteModel.getNoteTitle());
            cv.put("NOTE_COURSE", noteModel.getNoteCourse());
            cv.put("NOTE_CONTENT", noteModel.getNoteContent());

            long noteId = db.insert("NOTE_TABLE", null, cv);
            if (noteId == -1) {
                return false;
            }
        } finally {
            db.close();
        }
        return true;
    }

    public boolean updateNote(NoteModel noteModel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put("NOTE_TITLE", noteModel.getNoteTitle());
            cv.put("NOTE_COURSE", noteModel.getNoteCourse());
            cv.put("NOTE_CONTENT", noteModel.getNoteContent());

            int rowsAffected = db.update("NOTE_TABLE", cv, "NOTE_ID = ?", new String[]{String.valueOf(noteModel.getNoteID())});
            if (rowsAffected <= 0) {
                return false;
            }
        } finally {
            db.close();
        }
        return true;
    }
}
