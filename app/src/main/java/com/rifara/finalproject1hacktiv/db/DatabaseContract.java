package com.rifara.finalproject1hacktiv.db;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_NAME = "todolist";

    public static final String TABLE_NOTE = "table_note";

    public static final class TodoListColumns implements BaseColumns {
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String DATE = "date";

    }

}
