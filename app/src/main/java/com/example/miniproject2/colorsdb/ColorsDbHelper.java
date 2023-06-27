package com.example.miniproject2.colorsdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import androidx.annotation.Nullable;

import com.example.miniproject2.db.QuotesContract;
import com.example.miniproject2.modols.Colors;
import com.example.miniproject2.modols.Quote;

import java.util.ArrayList;

public class ColorsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Colors.db";
    private static final String SQL_CREATE_COLORS = String.format( "CREATE TABLE %s ("+
                    " %s INTEGER PRIMARY KEY,"+
                    " %s TEXT,"+
                    " %s TEXT)",
            ColorContract.colors.TABLE_NAME_COLORS,
            ColorContract.colors.COLUMN_NAME_ID,
            ColorContract.colors.COLUMN_NAME_COLOR,
            ColorContract.colors.COLUMN_NAME_CODE);
    private static final String SQL_CREATE_SETTING = String.format( "CREATE TABLE %s ("+
                    " %s INTEGER PRIMARY KEY,"+
                    " %s TEXT,"+
                    " %s TEXT)",
            ColorContract.settings.TABLE_NAME_SETTING,
            ColorContract.settings.COLUMN_ID_SETTING,
            ColorContract.settings.COLUMN_NAME_SETTING,
            ColorContract.settings.COLUMN_VALUE);



    private static final String SQL_DELETE_COLOR =String.format( "DROP TABLE IF EXISTS %s", ColorContract.colors.TABLE_NAME_COLORS);
    private static final String SQL_DELETE_SETTING =String.format( "DROP TABLE IF EXISTS %s", ColorContract.colors.TABLE_NAME_COLORS);


    public ColorsDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COLORS);
        db.execSQL(SQL_CREATE_SETTING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_COLOR);
        db.execSQL(SQL_DELETE_SETTING);
        onCreate(db);
    }
    private void add(int id, String color, String code ){
        SQLiteDatabase db = ColorsDbHelper.this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ColorContract.colors.COLUMN_NAME_ID, id);
        values.put(ColorContract.colors.COLUMN_NAME_COLOR, color);
        values.put(ColorContract.colors.COLUMN_NAME_CODE, code);

// Insert the new row, returning the primary key value of the new row
        db.insert(ColorContract.colors.TABLE_NAME_COLORS, null, values);
    }


    public void add(Colors color) {
        add(color.getId(), color.getName(), color.getCode());
    }
    public void addbgcolor(String name, String value) {
        SQLiteDatabase db = ColorsDbHelper.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ColorContract.settings.COLUMN_NAME_SETTING, name);
        values.put(ColorContract.settings.COLUMN_VALUE, value);

        int rowCount = (int) DatabaseUtils.queryNumEntries(db, ColorContract.settings.TABLE_NAME_SETTING);

        if (rowCount == 0) {
            db.insert(ColorContract.settings.TABLE_NAME_SETTING, null, values);
        } else {
            db.update(ColorContract.settings.TABLE_NAME_SETTING, values, null, null);
        }
    }




    public ArrayList<Colors> getAllcolors() {
        ArrayList<Colors> colors = new ArrayList<>();
        SQLiteDatabase db = ColorsDbHelper.this.getReadableDatabase();


        String[] projection = {
                ColorContract.colors.COLUMN_NAME_ID,
                ColorContract.colors.COLUMN_NAME_COLOR,
                ColorContract.colors.COLUMN_NAME_CODE
        };

        Cursor cursor = db.query(
                ColorContract.settings.TABLE_NAME_SETTING,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ColorContract.colors.COLUMN_NAME_ID));
            String color = cursor.getString(cursor.getColumnIndexOrThrow(ColorContract.colors.COLUMN_NAME_COLOR));
            String code = cursor.getString(cursor.getColumnIndexOrThrow(ColorContract.colors.COLUMN_NAME_CODE));
            colors.add(new Colors(id, color, code));
        }

        cursor.close();

        return colors;
    }
    public String getBg(){
        SQLiteDatabase db = ColorsDbHelper.this.getReadableDatabase();
        String code=null;
        String [] project = {
                ColorContract.settings.COLUMN_NAME_SETTING,
                ColorContract.settings.COLUMN_VALUE
        };
        Cursor cursour = db.query(
                ColorContract.settings.TABLE_NAME_SETTING,
                project,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursour.moveToNext()){
            code = cursour.getString(cursour.getColumnIndexOrThrow(ColorContract.settings.COLUMN_VALUE));

        }
        cursour.close();
        return code;
    }



}
