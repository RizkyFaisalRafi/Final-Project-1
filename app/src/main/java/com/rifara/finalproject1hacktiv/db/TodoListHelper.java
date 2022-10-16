package com.rifara.finalproject1hacktiv.db;

import static android.provider.UserDictionary.Words._ID;
import static com.rifara.finalproject1hacktiv.db.DatabaseContract.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// mengakomodasi kebutuhan DML
public class TodoListHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;
    // 1 metode yang nantinya akan digunakan untuk menginisiasi database.
    private static TodoListHelper INSTANCE;

    private TodoListHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }


    public static TodoListHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TodoListHelper(context);
                }
            }
        }
        return INSTANCE;
    }


    // metode untuk membuka dan menutup koneksi ke database-nya.
    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen()) {
            database.close();
        }

    }

    // metode untuk melakukan proses CRUD-nya, metode pertama adalah untuk mengambil data.
    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    // metode untuk mengambil data dengan id tertentu.
    public Cursor queryById(String id) {
        return database.query(
                DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    // metode untuk menyimpan data.
    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    // metode untuk memperbarui data.
    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    // metode untuk menghapus data.
    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = " + id, null);
    }

}
