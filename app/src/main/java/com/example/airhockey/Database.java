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

    public ArrayList<Skin> getallSkins() {
        ArrayList<Skin> skinsList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM skins", null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int priceIndex = cursor.getColumnIndex("price");
            int purchasedIndex = cursor.getColumnIndex("purchased");
            int selectedIndex = cursor.getColumnIndex("selected");

            do {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                int price = cursor.getInt(priceIndex);
                int purchased = cursor.getInt(purchasedIndex);
                int selected = cursor.getInt(selectedIndex);

                Skin skin = new Skin(id, name, price, purchased, selected);
                skinsList.add(skin);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return skinsList;
    }

    public void purchaseSkin(int skinIndex) {
        ContentValues values = new ContentValues();
        values.put("purchased", 1);
        values.put("selected", 1);
        db.update("skins", values, "id=?", new String[]{String.valueOf(skinIndex)});
    }

    public int getSelectedSkinIndex() {
        int index = -1;
        Cursor cursor = db.rawQuery("SELECT * from skins WHERE selected = 1", null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            index = cursor.getInt(idIndex);
        }
        cursor.close();
        return index;
    }

    public void deselectOldSkin() {
        int oldSelectedIndex = getSelectedSkinIndex();
        ContentValues values = new ContentValues();
        values.put("selected", 0);
        db.update("skins", values, "selected=?", new String[]{String.valueOf(oldSelectedIndex)});
    }

}
