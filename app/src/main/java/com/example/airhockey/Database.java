package com.example.airhockey;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private SQLiteDatabase db;
    private DataBaseHelper dbHelper;

    public Database(Context context) {
        dbHelper = new DataBaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertCoin(int amount) {
        ContentValues values = new ContentValues();
        values.put("amount", amount);
        db.insert("coins", null, values);
    }

    @SuppressLint("Range")
    public int getCoins() {
        int amount = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM coins", null);
        if (cursor.moveToFirst()) {
            amount = cursor.getInt(cursor.getColumnIndex("amount"));
        }
        cursor.close();
        return amount;
    }

    public List<Skin> getSkins() {
        List<Skin> skinsList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM skins", null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int priceIndex = cursor.getColumnIndex("price");
            int purchasedIndex = cursor.getColumnIndex("price");
            int imagePathIndex = cursor.getColumnIndex("pathToSkin");

            do {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                int price = cursor.getInt(priceIndex);
                int purchased = cursor.getInt(purchasedIndex);
                String imagePath = cursor.getString(imagePathIndex);

                Skin skin = new Skin(id, name, price, purchased, imagePath);
                skinsList.add(skin);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return skinsList;
    }
}
