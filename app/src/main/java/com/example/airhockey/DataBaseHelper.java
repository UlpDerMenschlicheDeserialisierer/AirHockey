package com.example.airhockey;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "airhockey.db";
    private static final int DATABASE_VERSION = 3;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS coins (amount INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE IF NOT EXISTS skins (id INTEGER PRIMARY KEY, name TEXT, price INTEGER, purchased INTEGER, selected INTEGER)");

        // Standard-Skins hinzufügen
        insertSkin(db,0, "BlueFire", 0,1, 1); // 1 = purchased, 0 = not purchased, 1 = selected, 0 = not selected
        insertSkin(db,1,"Red", 10, 0, 0);
        insertSkin(db,2, "Blue", 10,0, 0 );
        insertSkin(db,3, "Green", 20, 0, 0);
        insertSkin(db,4, "Yellow", 30, 0, 0);
        insertSkin(db,5, "Purple", 40, 0, 0);
        insertSkin(db,6, "Dark", 60, 0, 0);
        insertSkin(db,7, "Orange", 80, 0, 0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS coins");
        db.execSQL("DROP TABLE IF EXISTS skins");
        onCreate(db);
    }

    private void insertSkin(SQLiteDatabase db, int id, String name, int price,int purchased, int selected) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("purchased", purchased);
        contentValues.put("selected", selected);
        db.insert("skins", null, contentValues);
    }
}

