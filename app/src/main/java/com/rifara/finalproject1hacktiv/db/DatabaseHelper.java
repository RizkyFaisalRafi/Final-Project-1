package com.rifara.finalproject1hacktiv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbtodolistapp";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_TODOLIST = String.format("CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
                    DatabaseContract.TABLE_NAME,
            DatabaseContract.TodoListColumns._ID,
            DatabaseContract.TodoListColumns.TITLE,
            DatabaseContract.TodoListColumns.DESCRIPTION,
            DatabaseContract.TodoListColumns.DATE);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TODOLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NOTE);
        onCreate(db);
    }
}
