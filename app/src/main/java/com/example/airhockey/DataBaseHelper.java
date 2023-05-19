package com.example.airhockey;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "airhockey.db";
    private static final int DATABASE_VERSION = 15;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS coins (amount INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE IF NOT EXISTS skins (id INTEGER PRIMARY KEY, name TEXT, price INTEGER, purchased INTEGER, selected INTEGER)");
        ContentValues values = new ContentValues();
        values.put("amount", 0);
        db.insert("coins", null, values);
        // Standard-Skins hinzufügen
        insertSkin(db,0, "BlueFire", 0,1, 1); // 1 = purchased, 0 = not purchased, 1 = selected, 0 = not selected
        insertSkin(db,1,"Red", 10000, 0, 0);
        insertSkin(db,2, "Skin3", 3000,0, 0 );
        insertSkin(db,3, "Skin4", 4000, 0, 0);
        insertSkin(db,4, "Skin5", 3080, 0, 0);
        insertSkin(db,5, "Skin6", 4090, 0, 0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Lasse die Implementierung leer, da wir die Standardimplementierung nicht benötigen
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Lasse die Implementierung leer, da wir die Standardimplementierung nicht benötigen
    }

    private void insertSkin(SQLiteDatabase db, int id, String name, int price, int purchased, int selected) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("purchased", purchased);
        contentValues.put("selected", selected);
        db.insert("skins", null, contentValues);
    }
}
