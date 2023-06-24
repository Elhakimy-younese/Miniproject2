package com.example.miniproject2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class quotesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Quotes.db";
    private static final String SQL_CREATE_FAVORITE_QUOTES = String.format( "CREATE TABLE %s ("+
                    " %s INTEGER PRIMARY KEY,"+
                    " %s TEXT,"+
                    " %s TEXT)",
            QuotesContract.favoritequotes.TABLE_NAME,
            QuotesContract.favoritequotes.COLUMN_NAME_ID,
            QuotesContract.favoritequotes.COLUMN_NAME_QUOTES,
            QuotesContract.favoritequotes.COLUMN_NAME_AUTHOR);

    private static final String SQL_DELETE_FQVORITQUOTES =String.format( "DROP TABLE IF EXISTS %s", QuotesContract.favoritequotes.TABLE_NAME);


    public quotesDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITE_QUOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FQVORITQUOTES);
        onCreate(db);
    }
    public void add(int id, String quote, String author ){
        SQLiteDatabase db = quotesDbHelper.this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(QuotesContract.favoritequotes.COLUMN_NAME_ID, id);
        values.put(QuotesContract.favoritequotes.COLUMN_NAME_QUOTES, quote);
        values.put(QuotesContract.favoritequotes.COLUMN_NAME_AUTHOR, author);

// Insert the new row, returning the primary key value of the new row
        db.insert(QuotesContract.favoritequotes.TABLE_NAME, null, values);
    }
    public void getAll() {
        SQLiteDatabase db = quotesDbHelper.this.getReadableDatabase();

        String Cursor;
        String[] projection = {
                QuotesContract.favoritequotes.COLUMN_NAME_ID,
                QuotesContract.favoritequotes.COLUMN_NAME_QUOTES,
                QuotesContract.favoritequotes.COLUMN_NAME_AUTHOR
        };

        android.database.Cursor cursor = db.query(
                QuotesContract.favoritequotes.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(QuotesContract.favoritequotes.COLUMN_NAME_ID));
            String quote = cursor.getString(cursor.getColumnIndexOrThrow(QuotesContract.favoritequotes.COLUMN_NAME_ID));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(QuotesContract.favoritequotes.COLUMN_NAME_ID));
            Log.e("SQLITE", String.format("Quote %d, %s, %s", id, quote, author));
        }

        cursor.close();
    }

}
