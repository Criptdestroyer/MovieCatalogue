package com.criptdestroyer.moviecatalogueapi.model.db;


import android.database.Cursor;
import android.net.Uri;

public final class DatabaseContract {
    public static final String AUTHORITY = "com.criptdestroyer.moviecatalogueapi";
    public static String TABLE_FAV = "favorite";

    private DatabaseContract(){}

    public static final class FavColumns {
        public static String ID = "id";
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String DATE = "date";
        public static String PHOTO = "photo";
        public static String TYPE = "type";

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_FAV);
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
