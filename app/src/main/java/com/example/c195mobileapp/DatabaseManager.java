//package com.example.c195mobileapp;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//
//public class DatabaseManager {
//    private DatabaseHelper dbHelper;
//    private Context context;
//    private SQLiteDatabase database;
//    public DatabaseManager(Context ctx){
//        context = ctx;
//    }
//    public DatabaseManager open() throws {
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getWritableDatabase();
//        return this;
//    }
//    public void close() {
//        dbHelper.close();
//    }
//    public void insert (String username, String password){
//        ContentValues contentValues = new ContentValues();
//        ContentValues.put
//    }
//}
