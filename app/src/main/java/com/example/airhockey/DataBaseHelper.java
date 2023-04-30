package com.example.airhockey;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "airhockey.db";
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE coins (amount INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE skins (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER, purchased INTEGER, pathToSkin TEXT)");

        // Füge Standard-Skins hinzufügen
        insertSkin(db, "BlueFire", 0,1,"skins/default.png"); // 1 = purchased, 0 = not purchased
        insertSkin(db, "Red", 10, 0,"skins/red.png");
        insertSkin(db, "Blue", 10,0, "skins/blue.png");
        insertSkin(db, "Green", 20, 0,"skins/green.png");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS coins");
        db.execSQL("DROP TABLE IF EXISTS skins");
        onCreate(db);
    }

    private void insertSkin(SQLiteDatabase db, String name, int price,int purchased, String pathToSkin) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("purchased", purchased);
        contentValues.put("pathToSkin", pathToSkin);
        db.insert("skins", null, contentValues);
    }
}

