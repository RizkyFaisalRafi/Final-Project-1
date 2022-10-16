package com.rifara.finalproject1hacktiv.helper;

import android.database.Cursor;

import com.rifara.finalproject1hacktiv.db.DatabaseContract;
import com.rifara.finalproject1hacktiv.entity.TodoList;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<TodoList> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<TodoList> notesList = new ArrayList<>();
        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.TodoListColumns._ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.TodoListColumns.TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.TodoListColumns.DESCRIPTION));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.TodoListColumns.DATE));
            notesList.add(new TodoList(id, title, description, date));
        }
        return notesList;
    }
}
