package com.example.favoritemoviecatalogue.model.db;

import android.database.Cursor;
import android.net.Uri;

public class DatabaseContract {
    public static final String AUTHORITY = "com.criptdestroyer.moviecatalogueapi";
    private static final String SCHEME = "content";
    public static String TABLE_FAV = "favorite";

    private DatabaseContract(){}

    public static final class FavColumns {
        public static String ID = "id";
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String DATE = "date";
        public static String PHOTO = "photo";
        public static String TYPE = "type";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAV)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
